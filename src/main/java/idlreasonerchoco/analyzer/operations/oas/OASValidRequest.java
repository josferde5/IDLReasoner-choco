package idlreasonerchoco.analyzer.operations.oas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import idlreasonerchoco.configuration.ErrorType;
import idlreasonerchoco.configuration.IDLException;
import idlreasonerchoco.mapper.OASMapper;
import idlreasonerchoco.model.ParameterType;
import idlreasonerchoco.utils.ExceptionManager;
import idlreasonerchoco.utils.Utils;
import io.swagger.v3.oas.models.parameters.Parameter;

public class OASValidRequest implements AnalysisOperation {

    private static final Logger LOG = Logger.getLogger(OASValidRequest.class);

    private final OASMapper mapper;
    private final Map<String, String> request;
    private final boolean partial;

    public OASValidRequest(OASMapper mapper, Map<String, String> request, boolean partial) {
        this.mapper = mapper;
        this.request = request;
        this.partial = partial;
    }

    public boolean analyze() throws IDLException {
        try {
	        if (request != null && request.keySet().stream().allMatch(param -> mapper.getVariablesMap().containsKey(Utils.parseIDLParamName(param)))) {
	            List<Constraint> cons = new ArrayList<>();
	            mapper.getChocoModel().getSolver().reset();
	            
	        	for (Parameter parameter : mapper.getParameters()) {
	                BoolVar varSet = mapper.getVariablesMap().get(Utils.parseIDLParamName(parameter.getName()) + "Set").asBoolVar();
	                if (request.containsKey(parameter.getName())) {
	                    IntVar paramVar = mapper.getVariablesMap().get(Utils.parseIDLParamName(parameter.getName())).asIntVar();
	                    Constraint con = mapper.getChocoModel().and(mapper.getChocoModel().arithm(varSet, "=", 1),
	                            mapper.getChocoModel().arithm(paramVar, "=", mapValueToConstraint(request.get(parameter.getName()), parameter.getSchema().getType())));
	
	                    cons.add(con);
	                    con.post();
	                    
	                } else if (!partial) {
	                    Constraint setCon = mapper.getChocoModel().arithm(varSet, "=", 0);
	                    cons.add(setCon);
	                    setCon.post();
	                }
	            }
	            
	            boolean result = mapper.getChocoModel().getSolver().solve();
	            cons.forEach(x -> mapper.getChocoModel().unpost(x));
	            mapper.getChocoModel().getSolver().reset();
	            return result;
	            
	        } else {
	        	throw new IDLException(ErrorType.ERROR_UNKNOWN_PARAM_IN_REQUEST.toString());
	        }
	        
        } catch (IDLException e) {
        	ExceptionManager.log(LOG, ErrorType.ERROR_VALIDATING_REQUEST.toString(), e);
            return false;
        }
    }

    private Integer mapValueToConstraint(String paramValue, String type) throws IDLException {
    	try {
	        switch (ParameterType.valueOf(type.toUpperCase())) {
	            case STRING:
	            case ARRAY:
	                return mapper.getSolver().stringToInt(paramValue);
	            case NUMBER:
	            case INTEGER:
	                return Integer.valueOf(paramValue);
	            case BOOLEAN:
	            	if (Boolean.toString(true).equals(paramValue)) {
	            		return 1;
	            	} else if (Boolean.toString(false).equals(paramValue)) {
	            		return 0;
	            	}
	            default:
				throw new IDLException(
						ErrorType.ERROR_IN_PARAMETER_TYPE.toString() + " -> type: " + type + ", value: " + paramValue);
	        }
	        
    	} catch (Exception e) {
    		ExceptionManager.rethrow(LOG, ErrorType.ERROR_MAPPING_TO_CONSTRAINT.toString(), e);
            return null;
    	}
    }
}
