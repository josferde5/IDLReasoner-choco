package idlreasonerchoco.solver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Variable;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.common.collect.HashBiMap;
import com.google.inject.Injector;

import es.us.isa.idl.IDLStandaloneSetupGenerated;
import es.us.isa.idl.generator.IDLGenerator;
import es.us.isa.idl.generator.Response;
import idlreasonerchoco.configuration.ErrorType;
import idlreasonerchoco.configuration.IDLException;
import idlreasonerchoco.model.ParameterType;
import idlreasonerchoco.utils.ExceptionManager;
import idlreasonerchoco.utils.Utils;
import io.swagger.v3.oas.models.parameters.Parameter;

/**
 * OASSolver extends Solver class. This class is used by OASMapper class to do the following:
 * <ul>
 * <li>To use choco model.</li>
 * <li>To map string to integer value.</li>
 * <li>To get map of variables</li>
 * <li>To restart the solver</li>
 * </ul>
 * 
 * @see idlreasonerchoco.mapper.OASMapper
 */
public class OASSolver extends Solver {
	private static final Logger LOG = Logger.getLogger(OASSolver.class);

	private static final String DUMMY_URI = "dummy:/dummy.idl";
	private static final int MIN_INTEGER = -1000;
	private static final int MAX_INTEGER = 1000;
	private static final String EQUALS = "=";

	/**
	 * String to int HashBiMap
	 */
	private HashBiMap<String, Integer> stringToIntMap;

	/**
	 * Variable map
	 */
	private Map<String, Variable> variablesMap;
	private boolean valid = true;

	/**
	 * Creates OASSolver object.
	 * 
	 * @param data          map of variable name and list of its values.
	 * @param parameters    list of parameters
	 * @param operationPath operation path
	 * @param idl           IDL from open API specification.
	 * @param valid true or false.
	 * @throws IDLException IDL exception.
	 */
	public OASSolver(Map<String, List<String>> data, List<Parameter> parameters, String operationPath, String idl,
			boolean valid) throws IDLException {
		super(operationPath);
		this.valid = valid;
		this.variablesMap = new HashMap<>();
		this.stringToIntMap = HashBiMap.create();

		List<Constraint> requiredParameters = this.mapVariables(data, parameters);
		this.generateConstraintsFromIDL(idl, requiredParameters);

	}

	/**
	 * Creates OASSolver object.
	 * 
	 * @param data          map of variable name and list of its values.
	 * @param parameters    list of parameters
	 * @param operationPath operation path
	 * @param idl           IDL from open API specification.
	 * @throws IDLException IDL exception.
	 */
	public OASSolver(Map<String, List<String>> data, List<Parameter> parameters, String operationPath, String idl)
			throws IDLException {
		this(data, parameters, operationPath, idl, true);
	}

	/**
	 * Returns choco model.
	 * 
	 * @return choco model.
	 */
	public Model getChocoModel() {
		return chocoModel;
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
		return stringToIntMap;
	}

	/**
	 * Returns variables as a map.
	 * 
	 * <ul>
	 * <li><b>key</b> : variable name</li>
	 * <li><b>value</b> : Variable</li>
	 * </ul>
	 * 
	 * @return variables as a map.
	 */
	public Map<String, Variable> getVariablesMap() {
		return variablesMap;
	}

	/**
	 * Returns list of constraints.
	 * 
	 * @param data       map of variable name with its list of values.
	 * @param parameters list of parameters.
	 * @return list of constraints.
	 * @throws IDLException IDL exception.
	 */
	private List<Constraint> mapVariables(Map<String, List<String>> data, List<Parameter> parameters)
			throws IDLException {
		List<Constraint> requiredParameters = new ArrayList<>();
		for (Parameter parameter : parameters) {
			String paramType = parameter.getSchema().getType();
			List<?> paramEnum = parameter.getSchema().getEnum();
			BoolVar varParamSet = this
					.getVariable(Utils.parseIDLParamName(parameter.getName()) + "Set", BoolVar.class, false)
					.asBoolVar();

			if (paramType.equals(ParameterType.BOOLEAN.toString())) {
				this.getVariable(Utils.parseIDLParamName(parameter.getName()), BoolVar.class, false);
			} else if (paramEnum != null) {

				if (paramType.equals(ParameterType.STRING.toString())) {
					int[] domain = paramEnum.stream().mapToInt(x -> this.stringToInt(x.toString())).toArray();
					this.getVariable(Utils.parseIDLParamName(parameter.getName()), IntVar.class, true, domain);

				} else if (paramType.equals(ParameterType.INTEGER.toString())) {
					int[] domain = paramEnum.stream().mapToInt(x -> Integer.parseInt(x.toString())).toArray();
					this.getVariable(Utils.parseIDLParamName(parameter.getName()), IntVar.class, true, domain);

				} else {
					ExceptionManager.rethrow(LOG, ErrorType.ERROR_IN_PARAMETER_TYPE.toString() + " :" + paramType);
				}

			} else if (paramType.equals(ParameterType.STRING.toString())
					|| paramType.equals(ParameterType.ARRAY.toString())) {

				if (data != null && data.get(parameter.getName()) != null && !data.get(parameter.getName()).isEmpty()) {
					int[] domain = data.get(parameter.getName()).stream().mapToInt(x -> this.stringToInt(x.toString()))
							.toArray();
					this.getVariable(Utils.parseIDLParamName(parameter.getName()), IntVar.class, true, domain);
				} else {
					this.getVariable(Utils.parseIDLParamName(parameter.getName()), IntVar.class, false, 0, MAX_INTEGER);
				}

			} else if (paramType.equals(ParameterType.INTEGER.toString())
					|| paramType.equals(ParameterType.NUMBER.toString())) {
				this.getVariable(Utils.parseIDLParamName(parameter.getName()), IntVar.class, false,
						getMinimumValue(parameter), getMaximumValue(parameter));

			} else {
				ExceptionManager.rethrow(LOG, ErrorType.ERROR_IN_PARAMETER_TYPE.toString() + " :" + paramType);
			}

			if (Boolean.TRUE.equals(parameter.getRequired()) && valid) {
				this.chocoModel.arithm(varParamSet, EQUALS, 1).post();
			} else if (Boolean.TRUE.equals(parameter.getRequired())) {
				requiredParameters.add(this.chocoModel.arithm(varParamSet, EQUALS, 0));
			}
		}

		return requiredParameters;
	}

	/**
	 * Returns a maximum value for a parameter.
	 * 
	 * @param parameter
	 * @return maximum value for a parameter.
	 */
	private int getMaximumValue(Parameter parameter) {
		int maximum = parameter.getSchema().getMaximum() != null ? parameter.getSchema().getMaximum().intValue()
				: MAX_INTEGER;
		return parameter.getSchema().getExclusiveMaximum() != null && parameter.getSchema().getExclusiveMaximum()
				? maximum - 1
				: maximum;
	}

	/**
	 * Returns a minimum value for a parameter.
	 * 
	 * @param parameter
	 * @return minimum value for a parameter.
	 */
	private int getMinimumValue(Parameter parameter) {
		int minimum = parameter.getSchema().getMinimum() != null ? parameter.getSchema().getMinimum().intValue()
				: MIN_INTEGER;
		return parameter.getSchema().getExclusiveMinimum() != null && parameter.getSchema().getExclusiveMinimum()
				? minimum + 1
				: minimum;
	}

	/**
	 * Returns an Integer value.
	 * 
	 * @param stringValue string value
	 * @return Integer value.
	 */
	public Integer stringToInt(String stringValue) {
		Integer intMapping = stringToIntMap.get(stringValue);
		if (intMapping != null) {
			return intMapping;
		} else {
			int size = stringToIntMap.entrySet().size();
			stringToIntMap.put(stringValue, size);
			return size;
		}
	}

	/**
	 * Returns a variable.
	 * 
	 * @param name           variable name.
	 * @param type           class type
	 * @param absoluteDomain
	 * @param domain
	 * @return variable.
	 */
	private Variable getVariable(String name, Class<? extends Variable> type, boolean absoluteDomain, int... domain) {
		Variable paramVar = variablesMap.get(name);
		if (paramVar != null) {
			return paramVar;
		} else {
			if (type == BoolVar.class) {
				variablesMap.put(name, chocoModel.boolVar(name));
			} else if (type == IntVar.class) {
				if (absoluteDomain) {
					variablesMap.put(name, chocoModel.intVar(name, domain));
				} else if (domain.length <= 2) {
					variablesMap.put(name, chocoModel.intVar(name, domain.length >= 1 ? domain[0] : MIN_INTEGER,
							domain.length == 2 ? domain[1] : MAX_INTEGER));
				}
			}
			return variablesMap.get(name);
		}
	}

	/**
	 * Generate constraints from IDL.
	 * 
	 * @param idl                IDL from open API specification.
	 * @param requiredParameters list of required parameters.
	 * @throws IDLException IDL exception.
	 */
	private void generateConstraintsFromIDL(String idl, List<Constraint> requiredParameters) throws IDLException {
		IDLGenerator idlGenerator = new IDLGenerator(stringToIntMap, variablesMap, chocoModel);
		Injector injector = new IDLStandaloneSetupGenerated().createInjectorAndDoEMFRegistration();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		Resource resource = resourceSet.createResource(URI.createURI(DUMMY_URI));
		InputStream in = new ByteArrayInputStream(idl.getBytes());

		try {
			resource.load(in, resourceSet.getLoadOptions());
			Response response = idlGenerator.doGenerateChocoModel(resource, valid, requiredParameters);
			this.stringToIntMap = HashBiMap.create(response.getStringToIntMap());
			this.chocoModel = response.getChocoModel();
			this.chocoModel.getSolver().setRestartOnSolutions();
			this.chocoModel.getSolver()
					.setSearch(Search.randomSearch(
							variablesMap.values().stream().map(x -> x.asIntVar()).toArray(IntVar[]::new),
							System.currentTimeMillis()));

		} catch (Exception e) {
			ExceptionManager.rethrow(LOG, ErrorType.ERROR_MAPPING_CONSTRAINTS_FROM_IDL.toString(), e);
		}
	}

}
