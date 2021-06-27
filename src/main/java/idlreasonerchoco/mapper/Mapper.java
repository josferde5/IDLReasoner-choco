package idlreasonerchoco.mapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.chocosolver.solver.Model;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;

import es.us.isa.idl.IDLStandaloneSetupGenerated;
import es.us.isa.idl.generator.IDLGenerator;
import es.us.isa.idl.generator.Response;
import idlreasonerchoco.configuration.IDLConfiguration;
import idlreasonerchoco.configuration.model.ErrorType;
import idlreasonerchoco.configuration.model.IDLException;
import idlreasonerchoco.model.OperationType;
import idlreasonerchoco.utils.ExceptionManager;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;

public class Mapper {

	private static final Logger LOG = Logger.getLogger(Mapper.class);

	private static final String OAS_SPECIFICATION_TYPE = "oas";
	private static final String X_DEPENDENCIES = "x-dependencies";
	private static final String NEW_LINE = "\n";
	private static final String DUMMY_URI = "dummy:/dummy.idl";

	private final IDLConfiguration configuration;
	
	private String idlFromOas;
	private OpenAPI openApiSpecification;
	private Operation operation;
	private List<Parameter> parameters;
	private Map<String, Integer> stringToIntMap;
	private Model chocoModel;

	public Mapper(IDLConfiguration configuration) throws IDLException {
		this.configuration = configuration;

		if (!this.configuration.getSpecificationType().toLowerCase().equals(OAS_SPECIFICATION_TYPE)) {
			ExceptionManager.rethrow(LOG, ErrorType.BAD_SPECIFICATION.toString());
		}
		
		this.readOpenApiSpecification();
		this.generateIDLFromOAS();		
		this.generateConstraintsFromIDL();
		this.mapVariables();
	}

	private void mapVariables() {
	}

	private void readOpenApiSpecification() {
		ParseOptions options = new ParseOptions();
        options.setResolveFully(true);
        this.openApiSpecification = new OpenAPIV3Parser().readContents(this.configuration.getApiSpecification()).getOpenAPI();
        this.operation = getOasOperation(this.configuration.getOperationPath(), this.configuration.getOperationType());
        this.parameters = this.operation.getParameters(); // NullPointerException would be thrown on purpose, to stop program
        if (this.operation.getRequestBody() != null) {
            if (this.parameters == null)
                this.parameters = new ArrayList<>();
            this.parameters.addAll(getFormDataParameters(this.operation));
        }
	}
	
	@SuppressWarnings("unchecked")
	public void generateIDLFromOAS() throws IDLException {
        try {
        	List<String> IDLdeps = (List<String>) operation.getExtensions().get(X_DEPENDENCIES);
            
            if (IDLdeps.size() != 0) {
              	this.idlFromOas = String.join(NEW_LINE, IDLdeps);
            }
        } catch (Exception e) {
        	ExceptionManager.rethrow(LOG, ErrorType.ERROR_READING_DEPENDECIES.toString(), e);
        }
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Collection<Parameter> getFormDataParameters(Operation operation) {
        List<Parameter> formDataParameters = new ArrayList<>();
        Schema formDataBody;
        Map<String, Schema> formDataBodyProperties;

        try {
            formDataBody = operation.getRequestBody().getContent().get("application/x-www-form-urlencoded").getSchema();
            formDataBodyProperties = formDataBody.getProperties();
        } catch (NullPointerException e) {
            return formDataParameters;
        }

        for (Map.Entry<String, Schema> property: formDataBodyProperties.entrySet()) {
            Parameter parameter = new Parameter().name(property.getKey()).in("formData").required(formDataBody.getRequired().contains(property.getKey()));
            parameter.setSchema(new Schema().type(property.getValue().getType()));
            parameter.getSchema().setEnum(property.getValue().getEnum());
            formDataParameters.add(parameter);
        }

        return formDataParameters;
    }

	private void generateConstraintsFromIDL() throws IDLException {
		IDLGenerator idlGenerator = new IDLGenerator();		
		Injector injector = new IDLStandaloneSetupGenerated().createInjectorAndDoEMFRegistration();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		Resource resource = resourceSet.createResource(URI.createURI(DUMMY_URI));
		InputStream in = new ByteArrayInputStream(this.idlFromOas.getBytes());
		
		try {
			resource.load(in, resourceSet.getLoadOptions());
			Response response = idlGenerator.doGenerateChocoModel(resource, null, null);
			this.stringToIntMap = response.getStringToIntMap();
			this.chocoModel = response.getChocoModel();
		} catch (Exception e) {
			ExceptionManager.rethrow(LOG, ErrorType.ERROR_MAPPING_CONSTRAINTS_FROM_IDL.toString(), e);
		}
	}

	private Operation getOasOperation(String operationPath, String operationType) {
		PathItem item = this.openApiSpecification.getPaths().get(operationPath);

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
			ExceptionManager.log(LOG, ErrorType.BAD_OAS_OPERATION.toString());
			return null;
		}
	}

	public Model getChocoModel() {
		return chocoModel;
	}

	public Map<String, Integer> getStringToIntMap() {
		return stringToIntMap;
	}
	
}
