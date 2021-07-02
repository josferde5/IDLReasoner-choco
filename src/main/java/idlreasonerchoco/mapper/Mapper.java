package idlreasonerchoco.mapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Variable;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;

import es.us.isa.idl.IDLStandaloneSetupGenerated;
import es.us.isa.idl.generator.IDLGenerator;
import es.us.isa.idl.generator.ReservedWords;
import es.us.isa.idl.generator.Response;
import idlreasonerchoco.configuration.IDLConfiguration;
import idlreasonerchoco.configuration.model.ErrorType;
import idlreasonerchoco.configuration.model.IDLException;
import idlreasonerchoco.model.OperationType;
import idlreasonerchoco.model.ParameterType;
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
	
	private static final String FORM_DATA = "formData";
	private static final String OAS_SPECIFICATION_TYPE = "oas";
	private static final String X_DEPENDENCIES = "x-dependencies";
	private static final String NEW_LINE = "\n";
	private static final String DUMMY_URI = "dummy:/dummy.idl";
	private static final String APPLICATION_TYPE = "application/x-www-form-urlencoded";
	private static final int MIN_INTEGER = -1000;
	private static final int MAX_INTEGER = 1000;
	private static final String EQUALS = "=";


	private final IDLConfiguration configuration;
	
	private String idlFromOas;
	private OpenAPI openApiSpecification;
	private Operation operation;
	private List<Parameter> parameters;
	private Map<String, Integer> stringToIntMap;
	private Model chocoModel;
	private Map<String, Variable> variablesMap = new HashMap<>();

	public Mapper(IDLConfiguration configuration) throws IDLException {
		this.configuration = configuration;
		this.chocoModel = new Model(configuration.getOperationPath());
		this.variablesMap = new HashMap<>();
		this.stringToIntMap = new HashMap<>();

		if (!this.configuration.getSpecificationType().toLowerCase().equals(OAS_SPECIFICATION_TYPE)) {
			ExceptionManager.rethrow(LOG, ErrorType.BAD_SPECIFICATION.toString());
		}
		
		this.readOpenApiSpecification();
		this.generateIDLFromOAS();	
		this.mapVariables();
		this.generateConstraintsFromIDL();
	}

	private void mapVariables() throws IDLException {

	        for (Parameter parameter: parameters) {
	            String paramType = parameter.getSchema().getType();
	            List<?> paramEnum = parameter.getSchema().getEnum();
	            BoolVar varParamSet = this.getVariable(this.parseIDLParamName(parameter.getName() + "Set"), BoolVar.class, false).asBoolVar();

	            if(paramType.equals(ParameterType.BOOLEAN.toString())) {
	            	this.getVariable(this.parseIDLParamName(parameter.getName()), BoolVar.class, false);
	            	
	            } else if(paramEnum != null) {
	            	
	                if (paramType.equals(ParameterType.STRING.toString())) {
                        int[] domain = paramEnum.stream().mapToInt(x->this.stringToInt(x.toString())).toArray();
                        this.getVariable(this.parseIDLParamName(parameter.getName()), IntVar.class, true, domain);
                        
	                } else if (paramType.equals(ParameterType.INTEGER.toString())) {
	                	int[] domain = paramEnum.stream().mapToInt(x->Integer.parseInt(x.toString())).toArray();
                        this.getVariable(this.parseIDLParamName(parameter.getName()), IntVar.class, true, domain);
                        
	                } else {
	                	throw new IDLException(ErrorType.ERROR_IN_PARAMETER_TYPE.toString() + " :" + paramType);
	                }
	                
	            } else if(paramType.equals(ParameterType.STRING.toString()) || paramType.equals(ParameterType.ARRAY.toString())) {
                    this.getVariable(this.parseIDLParamName(parameter.getName()), IntVar.class, false, 0, stringToIntMap.entrySet().size());
                    
	            } else if (paramType.equals(ParameterType.INTEGER.toString()) || paramType.equals(ParameterType.NUMBER.toString())) {
                    this.getVariable(this.parseIDLParamName(parameter.getName()), IntVar.class, false);
                    
	            } else {
	                throw new IDLException(ErrorType.ERROR_IN_PARAMETER_TYPE.toString() + " :" + paramType);
	            }

	            if (Boolean.TRUE.equals(parameter.getRequired())) {
	                this.chocoModel.arithm(varParamSet, EQUALS, 1).post();
	            }
	        }
	}

	private void readOpenApiSpecification() {
		ParseOptions options = new ParseOptions();
        options.setResolveFully(true);
        this.openApiSpecification = new OpenAPIV3Parser().readContents(this.configuration.getApiSpecification()).getOpenAPI();
        this.operation = getOasOperation(this.configuration.getOperationPath(), this.configuration.getOperationType());
        this.parameters = this.operation.getParameters(); 
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
            formDataBody = operation.getRequestBody().getContent().get(APPLICATION_TYPE).getSchema();
            formDataBodyProperties = formDataBody.getProperties();
        } catch (NullPointerException e) {
            return formDataParameters;
        }

        for (Map.Entry<String, Schema> property: formDataBodyProperties.entrySet()) {
            Parameter parameter = new Parameter().name(property.getKey()).in(FORM_DATA).required(formDataBody.getRequired().contains(property.getKey()));
            parameter.setSchema(new Schema().type(property.getValue().getType()));
            parameter.getSchema().setEnum(property.getValue().getEnum());
            formDataParameters.add(parameter);
        }

        return formDataParameters;
    }

	private void generateConstraintsFromIDL() throws IDLException {
		IDLGenerator idlGenerator = new IDLGenerator(stringToIntMap, chocoModel);		
		Injector injector = new IDLStandaloneSetupGenerated().createInjectorAndDoEMFRegistration();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		Resource resource = resourceSet.createResource(URI.createURI(DUMMY_URI));
		InputStream in = new ByteArrayInputStream(this.idlFromOas.getBytes());
		
		try {
			resource.load(in, resourceSet.getLoadOptions());
			Response response = idlGenerator.doGenerateChocoModel(resource);
			this.stringToIntMap = response.getStringToIntMap();
			this.chocoModel = response.getChocoModel();
			
			//TODO Borrar Codigo para ver las soluciones
			Solver solver = chocoModel.getSolver();
			Solution solution = new Solution(chocoModel);
			Set<IntVar> ints = new HashSet<>();
			Set<String> st = new HashSet<>();
			while(solver.solve()) {
				for(IntVar i : solution.record().retrieveIntVars(true)) {
					if(i.getName() != null && !i.getName().contains("cst") && !i.getName().contains("EQ") && !i.getName().contains("REIF") && !i.getName().contains("IV") && !st.contains(i.getName())) {
						ints.add(i);
						st.add(i.getName());
					}
				}
				for(IntVar i : ints) {
					System.out.println(i.getName() + EQUALS + i.getValue());
				}
				solver.setRestartOnSolutions();

				System.out.println("=========");
			} 
			if(solver.hasEndedUnexpectedly()){
			    System.out.println("The solver could not find a solution nor prove that none exists in the given limits");
			} else {
			    System.out.println("The solver has proved the problem has no solution");
			}
			
			int breakpoint = 1;
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

	private String parseIDLParamName(String paramName) {
		String parsedParamName = paramName.replaceAll("^\\[|\\]$", "").replaceAll("[\\.\\-\\/\\:\\[\\]]", "_");
		if (ReservedWords.RESERVED_WORDS.contains(parsedParamName))
			parsedParamName += "_R";
		return parsedParamName;
	}

	private Integer stringToInt(String stringValue) {
		Integer intMapping = stringToIntMap.get(stringValue);
		if (intMapping != null) {
			return intMapping;
		} else {
			int size = stringToIntMap.entrySet().size();
			stringToIntMap.put(stringValue, size);
			return size;
		}
	}
	
	private Variable getVariable(String name, Class<? extends Variable> type, boolean absoluteDomain, int... domain) {
		Variable paramVar = variablesMap.get(name);
		if (paramVar != null) {
			return paramVar;
		} else {
			if(type == BoolVar.class){
				variablesMap.put(name, chocoModel.boolVar(name));
			} else if (type == IntVar.class){
				if(absoluteDomain) {
					variablesMap.put(name, chocoModel.intVar(name, domain));
				} else if(domain.length<=2) {
					variablesMap.put(name, chocoModel.intVar(name, domain.length >= 1 ? domain[0] : MIN_INTEGER, domain.length==2 ? domain[1] : MAX_INTEGER));
				}
			}
			return variablesMap.get(name);
		}
	}
	
	public Model getChocoModel() {
		return chocoModel;
	}

	public Map<String, Integer> getStringToIntMap() {
		return stringToIntMap;
	}
	
}
