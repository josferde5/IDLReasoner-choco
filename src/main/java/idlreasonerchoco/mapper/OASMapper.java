package idlreasonerchoco.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.Variable;

import com.google.common.collect.HashBiMap;

import idlreasonerchoco.configuration.ErrorType;
import idlreasonerchoco.configuration.IDLConfiguration;
import idlreasonerchoco.configuration.IDLException;
import idlreasonerchoco.model.OperationType;
import idlreasonerchoco.utils.ExceptionManager;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.parser.core.models.ParseOptions;

public class OASMapper extends Mapper {
    private static final String BODY_EXTENSION = "_body";

	private static final Logger LOG = Logger.getLogger(OASMapper.class);

    private static final String FORM_DATA = "formData";
    private static final String OAS_SPECIFICATION_TYPE = "oas";
    private static final String X_DEPENDENCIES = "x-dependencies";
    private static final String NEW_LINE = "\n";
    private static final String APPLICATION_TYPE = "application/x-www-form-urlencoded";

    private String idlFromOas;
    private OpenAPI openApiSpecification;
    private Operation operation;
    private List<Parameter> parameters;
    private OASSolver solver;
    Map<String, List<String>> data;
    
    public OASMapper(IDLConfiguration configuration, Map<String, List<String>> data) throws IDLException {
        super(configuration);
        
        if (!this.configuration.getSpecificationType().toLowerCase().equals(OAS_SPECIFICATION_TYPE)) {
            ExceptionManager.rethrow(LOG, ErrorType.BAD_SPECIFICATION.toString());
        }

        this.readOpenApiSpecification();
        this.generateIDLFromOAS();
        this.updateData(data);
    }
    
    public OASMapper(IDLConfiguration configuration) throws IDLException {
    	this(configuration, null);
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
        
        if(formDataBodyProperties == null) {
        	String body = this.configuration.getOperationPath().replace("/", "_").substring(1) + BODY_EXTENSION;
        	formDataBody = this.openApiSpecification.getComponents().getSchemas().get(body);
        	formDataBodyProperties = formDataBody.getProperties();
        }

        for (Map.Entry<String, Schema> property : formDataBodyProperties.entrySet()) {
            Parameter parameter = new Parameter().name(property.getKey()).in(FORM_DATA).required(formDataBody.getRequired().contains(property.getKey()));
            parameter.setSchema(new Schema().type(property.getValue().getType()));
            parameter.getSchema().setEnum(property.getValue().getEnum());
            formDataParameters.add(parameter);
        }

        return formDataParameters;
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
    
    public OASSolver getSolver() {
    	return solver;
    }    
    
    public void updateData(Map<String, List<String>> data) throws IDLException {
    	this.data = data;
    	restartSolver();
    }
    
    public void restartSolver() throws IDLException {
    	this.solver = new OASSolver(data, parameters, configuration.getOperationPath(), idlFromOas);
    }

	public Model getChocoModel() {
		return this.solver.getChocoModel();
	}    
	
	public Map<String, Variable> getVariablesMap() {
		return this.solver.getVariablesMap();
	}    
	
	public HashBiMap<String, Integer> getStringToIntMap() {
		return this.solver.getStringToIntMap();
	}    

}
