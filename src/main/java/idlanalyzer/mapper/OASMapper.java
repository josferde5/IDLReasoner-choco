package idlanalyzer.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.Variable;
import org.eclipse.osgi.service.resolver.BundleSpecification;

import com.google.common.collect.HashBiMap;

import idlanalyzer.configuration.ErrorType;
import idlanalyzer.configuration.IDLConfiguration;
import idlanalyzer.configuration.IDLException;
import idlanalyzer.model.OperationType;
import idlanalyzer.solver.OASSolver;
import idlanalyzer.utils.ExceptionManager;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.parser.core.models.ParseOptions;

/**
 * OASMapper class extends Mapper class.
 * 
 * This class initializes a configuration object and based on that it reads and
 * generates IDL from open API specification.
 * 
 * Also,it has an OASSolver object as a member which is used to do the
 * following:
 * <ul>
 * <li>To use choco model.</li>
 * <li>To map string to integer value.</li>
 * <li>To get map of variables.</li>
 * <li>To restart the solver.</li>
 * </ul>
 *
 * @see OASSolver
 */
public class OASMapper extends Mapper {
	private static final String BODY_EXTENSION = "_body";

	private static final Logger LOG = Logger.getLogger(OASMapper.class);

	private static final String FORM_DATA = "formData";
	private static final String OAS_SPECIFICATION_TYPE = "oas";
	private static final String X_DEPENDENCIES = "x-dependencies";
	private static final String NEW_LINE = "\n";
	private static final String APPLICATION_TYPE = "application/x-www-form-urlencoded";

	/**
	 * IDL from open API specification.
	 */
	private String idlFromOas;

	/**
	 * Open API specification.
	 */
	private OpenAPI openApiSpecification;

	/**
	 * Operation object.
	 */
	private Operation operation;

	/**
	 * List of parameters.
	 */
	private List<Parameter> parameters;

	/**
	 * OASSolver object.
	 */
	private OASSolver solver;

	/**
	 * Map of parameters and list of values.
	 */
	private Map<String, List<String>> data;

	/**
	 * Creates OASMapper object.
	 * 
	 * @param configuration IDLConfiguration object.
	 * @param data          consist of map of parameters and list of values.
	 * @throws IDLException IDL exception.
	 */
	public OASMapper(IDLConfiguration configuration, Map<String, List<String>> data) throws IDLException {
		super(configuration);

		if (!this.configuration.getSpecificationType().toLowerCase().equals(OAS_SPECIFICATION_TYPE)) {
			ExceptionManager.rethrow(LOG, ErrorType.BAD_SPECIFICATION.toString());
		}

		this.readOpenApiSpecification();
		this.generateIDLFromOAS();
		this.updateData(data);
	}

	/**
	 * Creates OASMapper object.
	 * 
	 * @param configuration IDLConfiguration object.
	 * @throws IDLException IDL exception.
	 */
	public OASMapper(IDLConfiguration configuration) throws IDLException {
		this(configuration, null);
	}

	/**
	 * Reads open API specification.
	 * 
	 * @throws IDLException IDL exception.
	 */
	private void readOpenApiSpecification() throws IDLException {
		ParseOptions parseOptions = new ParseOptions();
		parseOptions.setResolveFully(true);
		parseOptions.setFlatten(true);
		if (this.configuration.isSpecAsString()) {
			this.openApiSpecification = new OpenAPIParser()
					.readContents(this.configuration.getApiSpecification(), null, parseOptions).getOpenAPI();
		} else {
			this.openApiSpecification = new OpenAPIParser()
					.readLocation(this.configuration.getApiSpecification(), null, parseOptions).getOpenAPI();
		}
		this.operation = getOasOperation(this.configuration.getOperationPath(), this.configuration.getOperationType());
		this.parameters = this.operation.getParameters() != null ? this.operation.getParameters() : new ArrayList<>();
		if (this.operation.getRequestBody() != null) {
			this.parameters.addAll(getFormDataParameters(this.operation));
		}
	}

	/**
	 * Generates IDL from open API specification.
	 */
	@SuppressWarnings("unchecked")
	public void generateIDLFromOAS() {
		try {
			List<String> IDLdeps = (List<String>) operation.getExtensions().get(X_DEPENDENCIES);
			if (!IDLdeps.isEmpty()) {
				this.idlFromOas = String.join(NEW_LINE, IDLdeps);
			}
		} catch (NullPointerException e) {
			this.idlFromOas = NEW_LINE;
		} catch (Exception e) {
			ExceptionManager.log(LOG, ErrorType.ERROR_READING_DEPENDECIES.toString(), e);
		}
	}

	/**
	 * Returns collection of parameters from an operation.
	 * 
	 * @param operation Operation object.
	 * @return collection of parameters.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Collection<Parameter> getFormDataParameters(Operation operation) {
		List<Parameter> formDataParameters = new ArrayList<>();
		Schema formDataBody;
		Map<String, Schema> formDataBodyProperties;

		try {
			formDataBody = operation.getRequestBody().getContent().get(APPLICATION_TYPE).getSchema();
			formDataBodyProperties = formDataBody.getProperties();
		} catch (NullPointerException e) {
			return formDataParameters;
		}

		if (formDataBodyProperties == null) {
			String body = this.configuration.getOperationPath().replace("/", "_").substring(1) + BODY_EXTENSION;
			formDataBody = this.openApiSpecification.getComponents().getSchemas().get(body);
			formDataBodyProperties = formDataBody.getProperties();
		}

		for (Map.Entry<String, Schema> property : formDataBodyProperties.entrySet()) {
			Parameter parameter = new Parameter().name(property.getKey()).in(FORM_DATA)
					.required(formDataBody.getRequired().contains(property.getKey()));
			parameter.setSchema(new Schema().type(property.getValue().getType()));
			parameter.getSchema().setEnum(property.getValue().getEnum());
			formDataParameters.add(parameter);
		}

		return formDataParameters;
	}

	/**
	 * Returns operation object.
	 * 
	 * @param operationPath operation path.
	 * @param operationType operation type.
	 * @return operation object.
	 * @throws IDLException IDL exception.
	 */
	private Operation getOasOperation(String operationPath, String operationType) throws IDLException {
		PathItem item = this.openApiSpecification.getPaths().get(operationPath);
		if (item != null) {
			try {
				switch (OperationType.valueOf(operationType.toUpperCase())) {
				case GET:
					return item.getGet();
				case DELETE:
					return item.getDelete();
				case HEAD:
					return item.getHead();
				case OPTIONS:
					return item.getOptions();
				case PATCH:
					return item.getPatch();
				case POST:
					return item.getPost();
				case PUT:
					return item.getPut();
				default:
					ExceptionManager.rethrow(LOG, ErrorType.BAD_OAS_OPERATION.toString());
					return null;
				}
			} catch (IllegalArgumentException e) {
				ExceptionManager.rethrow(LOG, ErrorType.BAD_OAS_OPERATION.toString());
				return null;
			}
		} else {
			ExceptionManager.rethrow(LOG, ErrorType.ERROR_OPERATION_PATH.toString());
			return null;
		}
	}

	/**
	 * Returns IDL from open API specification.
	 * 
	 * @return IDL from open API specification.
	 */
	public String getIdlFromOas() {
		return idlFromOas;
	}

	/**
	 * Returns open API specification.
	 * 
	 * @return open API specification.
	 */
	public OpenAPI getOpenApiSpecification() {
		return openApiSpecification;
	}

	/**
	 * Returns an Operation object. This object describes a single API operation on
	 * a path.
	 * 
	 * @return Operation object.
	 */
	public Operation getOperation() {
		return operation;
	}

	/**
	 * Returns list of Parameter objects. This object describes a single operation
	 * parameter.
	 * 
	 * @return list of Parameter objects.
	 */
	public List<Parameter> getParameters() {
		return parameters;
	}

	/**
	 * Returns OASSolver solver.
	 * 
	 * @return OASSolver solver.
	 * @see OASSolver
	 */
	public OASSolver getSolver() {
		return solver;
	}

	/**
	 * Updates parameters data as a map. This map contains parameter name as key and
	 * list of parameter values as value.
	 * 
	 * <ul>
	 * <li><b>key</b> : parameter name</li>
	 * <li><b>value</b> : list of values</li>
	 * </ul>
	 * 
	 * @param data to be updated
	 * @throws IDLException IDL exception.
	 */
	public void updateData(Map<String, List<String>> data) throws IDLException {
		this.data = data;
		restartSolver(true);
	}

	/**
	 * Restart OASSolver object.
	 * 
	 * @param valid true or false.
	 * @throws IDLException IDL exception
	 */
	public void restartSolver(boolean valid) throws IDLException {
		this.solver = new OASSolver(data, parameters, configuration.getOperationPath(), idlFromOas, valid);
	}

	/**
	 * Returns choco model.
	 * 
	 * @return choco model.
	 */
	public Model getChocoModel() {
		return this.solver.getChocoModel();
	}

	/**
	 * Returns variables as a map.
	 * 
	 * <ul>
	 * <li><b>key</b> : variable name</li>
	 * <li><b>value</b> : Variable</li>
	 * </ul>
	 * 
	 * @return Map of variables.
	 */
	public Map<String, Variable> getVariablesMap() {
		return this.solver.getVariablesMap();
	}

	/**
	 * Maps string to integer value.
	 * 
	 * <ul>
	 * <li><b>key</b> : a string</li>
	 * <li><b>value</b> : an integer value</li>
	 * </ul>
	 * 
	 * @return Maps string to integer value.
	 */
	public HashBiMap<String, Integer> getStringToIntMap() {
		return this.solver.getStringToIntMap();
	}

}
