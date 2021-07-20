package idlreasonerchoco.solver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.chocosolver.solver.Model;
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

public class OASSolver extends Solver {
	private static final Logger LOG = Logger.getLogger(OASSolver.class);

    private static final String DUMMY_URI = "dummy:/dummy.idl";
    private static final int MIN_INTEGER = -1000;
    private static final int MAX_INTEGER = 1000;
    private static final String EQUALS = "=";

    private HashBiMap<String, Integer> stringToIntMap;
    private Map<String, Variable> variablesMap;

	public OASSolver(Map<String, List<String>> data, List<Parameter> parameters, String operationPath, String idl) throws IDLException {
        super(operationPath);
        this.variablesMap = new HashMap<>();
        this.stringToIntMap = HashBiMap.create();
        this.stringToInt("default");
        
        this.mapVariables(data, parameters);
        this.generateConstraintsFromIDL(idl);
    }


	public Model getChocoModel() {
        return chocoModel;
    }
    
    public HashBiMap<String, Integer> getStringToIntMap() {
        return stringToIntMap;
    }
    
    public Map<String, Variable> getVariablesMap() {
        return variablesMap;
    }
        
    private void mapVariables(Map<String, List<String>> data, List<Parameter> parameters) throws IDLException {
        for (Parameter parameter : parameters) {
            String paramType = parameter.getSchema().getType();
            List<?> paramEnum = parameter.getSchema().getEnum();
            BoolVar varParamSet = this.getVariable(Utils.parseIDLParamName(parameter.getName()) + "Set", BoolVar.class, false).asBoolVar();

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

            } else if (paramType.equals(ParameterType.STRING.toString()) || paramType.equals(ParameterType.ARRAY.toString())) {
            	
            	if(data != null && data.get(parameter.getName()) != null && !data.get(parameter.getName()).isEmpty()) {
            		int[] domain = data.get(parameter.getName()).stream().mapToInt(x -> this.stringToInt(x.toString())).toArray();
                    this.getVariable(Utils.parseIDLParamName(parameter.getName()), IntVar.class, true, domain);
            	} else {
                    this.getVariable(Utils.parseIDLParamName(parameter.getName()), IntVar.class, false, 0, MAX_INTEGER);
            	}
            	
            } else if (paramType.equals(ParameterType.INTEGER.toString()) || paramType.equals(ParameterType.NUMBER.toString())) {
                this.getVariable(Utils.parseIDLParamName(parameter.getName()), IntVar.class, false, getMinimumValue(parameter), getMaximumValue(parameter));

            } else {
                ExceptionManager.rethrow(LOG, ErrorType.ERROR_IN_PARAMETER_TYPE.toString() + " :" + paramType);
            }

            if (Boolean.TRUE.equals(parameter.getRequired())) {
                this.chocoModel.arithm(varParamSet, EQUALS, 1).post();
            }
        }
    }
    

    private int getMaximumValue(Parameter parameter) {
        int maximum = parameter.getSchema().getMaximum() != null ? parameter.getSchema().getMaximum().intValue() : MAX_INTEGER;
        return parameter.getSchema().getExclusiveMaximum() != null && parameter.getSchema().getExclusiveMaximum()? maximum - 1 : maximum;
    }

    private int getMinimumValue(Parameter parameter) {
        int minimum = parameter.getSchema().getMinimum() != null ? parameter.getSchema().getMinimum().intValue() : MIN_INTEGER;
        return parameter.getSchema().getExclusiveMinimum() != null && parameter.getSchema().getExclusiveMinimum()? minimum + 1 : minimum;
    }


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
                    variablesMap.put(name, chocoModel.intVar(name, domain.length >= 1 ? domain[0] : MIN_INTEGER, domain.length == 2 ? domain[1] : MAX_INTEGER));
                }
            }
            return variablesMap.get(name);
        }
    }
    
    private void generateConstraintsFromIDL(String idl) throws IDLException {
        IDLGenerator idlGenerator = new IDLGenerator(stringToIntMap, variablesMap, chocoModel);
        Injector injector = new IDLStandaloneSetupGenerated().createInjectorAndDoEMFRegistration();
        XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
        Resource resource = resourceSet.createResource(URI.createURI(DUMMY_URI));
        InputStream in = new ByteArrayInputStream(idl.getBytes());

        try {
            resource.load(in, resourceSet.getLoadOptions());
            Response response = idlGenerator.doGenerateChocoModel(resource);
            this.stringToIntMap = HashBiMap.create(response.getStringToIntMap());
            this.chocoModel = response.getChocoModel();
            this.chocoModel.getSolver().setRestartOnSolutions();
            this.chocoModel.getSolver().setSearch(
					Search.randomSearch(variablesMap.values().stream().map(x -> x.asIntVar()).toArray(IntVar[]::new), 
					System.currentTimeMillis()));
            
        } catch (Exception e) {
            ExceptionManager.rethrow(LOG, ErrorType.ERROR_MAPPING_CONSTRAINTS_FROM_IDL.toString(), e);
        }
    }

}
