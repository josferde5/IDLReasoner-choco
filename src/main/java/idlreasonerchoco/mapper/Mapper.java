package idlreasonerchoco.mapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
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
import idlreasonerchoco.configuration.IDLConfiguration;
import idlreasonerchoco.configuration.model.ErrorType;
import idlreasonerchoco.configuration.model.IDLException;
import idlreasonerchoco.model.OperationType;
import idlreasonerchoco.model.ParameterType;
import idlreasonerchoco.utils.ExceptionManager;
import idlreasonerchoco.utils.Utils;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
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
    private HashBiMap<String, Integer> stringToIntMap;
    private Model chocoModel;
    private Map<String, Variable> variablesMap;

    public Mapper(IDLConfiguration configuration) throws IDLException {
        this.configuration = configuration;
        this.chocoModel = new Model(configuration.getOperationPath());
        this.variablesMap = new HashMap<>();
        this.stringToIntMap = HashBiMap.create();

        if (!this.configuration.getSpecificationType().toLowerCase().equals(OAS_SPECIFICATION_TYPE)) {
            ExceptionManager.rethrow(LOG, ErrorType.BAD_SPECIFICATION.toString());
        }

        this.readOpenApiSpecification();
        this.generateIDLFromOAS();
            this.mapVariables();
        this.generateConstraintsFromIDL();
    }

    private void mapVariables() throws IDLException {
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
                this.getVariable(Utils.parseIDLParamName(parameter.getName()), IntVar.class, false, 0, stringToIntMap.entrySet().size());

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
        int maximum = parameter.getSchema().getMaximum() != null? parameter.getSchema().getMaximum().intValue() : 1000;
        return parameter.getSchema().getExclusiveMaximum() != null && parameter.getSchema().getExclusiveMaximum()? maximum - 1 : maximum;
    }

    private int getMinimumValue(Parameter parameter) {
        int minimum = parameter.getSchema().getMinimum() != null? parameter.getSchema().getMinimum().intValue() : -1000;
        return parameter.getSchema().getExclusiveMinimum() != null && parameter.getSchema().getExclusiveMinimum()? minimum + 1 : minimum;
    }

    private void readOpenApiSpecification() throws IDLException {
        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolveFully(true);
        parseOptions.setFlatten(true);
        if (this.configuration.isSpecAsString()) {
            this.openApiSpecification = new OpenAPIParser().readContents(this.configuration.getApiSpecification(), null, parseOptions).getOpenAPI();
        } else {
            this.openApiSpecification = new OpenAPIParser().readLocation(this.configuration.getApiSpecification(), null, parseOptions).getOpenAPI();
        }
        this.operation = getOasOperation(this.configuration.getOperationPath(), this.configuration.getOperationType());
        this.parameters = this.operation.getParameters() != null? this.operation.getParameters() : new ArrayList<>();
        if (this.operation.getRequestBody() != null) {
            this.parameters.addAll(getFormDataParameters(this.operation));
        }
    }

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

    @SuppressWarnings({"unchecked", "rawtypes"})
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

        for (Map.Entry<String, Schema> property : formDataBodyProperties.entrySet()) {
            Parameter parameter = new Parameter().name(property.getKey()).in(FORM_DATA).required(formDataBody.getRequired().contains(property.getKey()));
            parameter.setSchema(new Schema().type(property.getValue().getType()));
            parameter.getSchema().setEnum(property.getValue().getEnum());
            formDataParameters.add(parameter);
        }

        return formDataParameters;
    }

    private void generateConstraintsFromIDL() throws IDLException {
        IDLGenerator idlGenerator = new IDLGenerator(stringToIntMap, variablesMap, chocoModel);
        Injector injector = new IDLStandaloneSetupGenerated().createInjectorAndDoEMFRegistration();
        XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
        Resource resource = resourceSet.createResource(URI.createURI(DUMMY_URI));
        InputStream in = new ByteArrayInputStream(this.idlFromOas.getBytes());

        try {
            resource.load(in, resourceSet.getLoadOptions());
            Response response = idlGenerator.doGenerateChocoModel(resource);
            this.stringToIntMap = HashBiMap.create(response.getStringToIntMap());
            this.chocoModel = response.getChocoModel();
            this.chocoModel.getSolver().setRestartOnSolutions();
            this.chocoModel.getSolver().setSearch(
					Search.randomSearch(variablesMap.values().stream().map(x -> x.asIntVar()).toArray(IntVar[]::new), 
					System.currentTimeMillis() * (long)(Math.random()*11)));
            
        } catch (Exception e) {
            ExceptionManager.rethrow(LOG, ErrorType.ERROR_MAPPING_CONSTRAINTS_FROM_IDL.toString(), e);
        }
    }

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

    public Model getChocoModel() {
        return chocoModel;
    }

    public HashBiMap<String, Integer> getStringToIntMap() {
        return stringToIntMap;
    }

    public String getIdlFromOas() {
        return idlFromOas;
    }

    public OpenAPI getOpenApiSpecification() {
        return openApiSpecification;
    }

    public Operation getOperation() {
        return operation;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public Map<String, Variable> getVariablesMap() {
        return variablesMap;
    }

}
