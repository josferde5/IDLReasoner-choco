package idlreasonerchoco.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import idlreasonerchoco.configuration.IDLConfiguration;
import idlreasonerchoco.configuration.model.ErrorType;
import idlreasonerchoco.configuration.model.IDLException;
import idlreasonerchoco.mapper.Mapper;
import idlreasonerchoco.model.ParameterType;
import idlreasonerchoco.utils.ExceptionManager;
import idlreasonerchoco.utils.Utils;
import io.swagger.v3.oas.models.parameters.Parameter;

public class Analyzer {

    private static final Logger LOG = Logger.getLogger(Analyzer.class);

    private final Mapper mapper;
    private final IDLConfiguration configuration;

    public Analyzer(String specificationType, String apiSpecification, String operationPath, String operationType) throws IDLException {
        this(specificationType, apiSpecification, operationPath, operationType, true);
    }

    public Analyzer(String specificationType, String apiSpecification, String operationPath, String operationType, boolean chargeFromFile) throws IDLException {
        this.configuration = new IDLConfiguration(specificationType, apiSpecification, operationPath, operationType, chargeFromFile);
        this.mapper = new Mapper(configuration);
    }

    public boolean isConsistent() {
        return mapper.getChocoModel().getSolver().solve();
    }

    public boolean isDeadParameter(String paramName) {
        BoolVar varSet = mapper.getVariablesMap().get(Utils.parseIDLParamName(paramName) + "Set").asBoolVar();
        Constraint cons = mapper.getChocoModel().arithm(varSet, "=", 1);
        cons.post();
        boolean result = !mapper.getChocoModel().getSolver().solve();
        mapper.getChocoModel().unpost(cons);
        return result;
    }

    public boolean isFalseOptional(String paramName) {
        BoolVar varSet = mapper.getVariablesMap().get(Utils.parseIDLParamName(paramName) + "Set").asBoolVar();
        Constraint cons = mapper.getChocoModel().arithm(varSet, "=", 0);
        cons.post();
        boolean result = isConsistent() && !mapper.getChocoModel().getSolver().solve();
        mapper.getChocoModel().unpost(cons);
        return result;
    }

    public Boolean isValidIDL() {
        return isConsistent() && mapper.getParameters().stream()
                .allMatch(param -> !isDeadParameter(param.getName()) && !isFalseOptional(param.getName()));
    }

    public Map<String, String> getRandomRequest() throws IDLException {
        mapper.getChocoModel().getSolver().reset();
        return mapRequest();
    }
    
    public Map<String, String> getInvalidRandomRequest() throws IDLException {
    	mapper.getChocoModel().getSolver().reset();
    	Stream.of(mapper.getChocoModel().getCstrs()).forEach(x->{
    		mapper.getChocoModel().unpost(x);
    		x.getOpposite().post();
    	});
    	
    	Map<String, String> request = mapRequest();
    	
    	Stream.of(mapper.getChocoModel().getCstrs()).forEach(x->{
    		mapper.getChocoModel().unpost(x);
    		x.getOpposite().post();
    	});
    	
    	return request;
    }
    
	private Map<String, String> mapRequest() throws IDLException {
		Map<String, String> request = null;
		if (isConsistent()) {
			request = new HashMap<>();
            for (Parameter parameter : mapper.getParameters()) {
                BoolVar varSet = mapper.getVariablesMap().get(Utils.parseIDLParamName(parameter.getName()) + "Set").asBoolVar();
                if (varSet.getValue() == 1) {
                    IntVar paramVar = mapper.getVariablesMap().get(Utils.parseIDLParamName(parameter.getName())).asIntVar();
                    request.put(parameter.getName(), mapConstraintToValue(paramVar.getValue(), parameter.getSchema().getType()));
                }
            }
        }
		return request;
	}

    public boolean isValidRequest(Map<String, String> request) throws IDLException {
        return isValidRequest(request, false);
    }

    public boolean isValidPartialRequest(Map<String, String> request) throws IDLException {
        return isValidRequest(request, true);
    }

    private boolean isValidRequest(Map<String, String> request, boolean partial) throws IDLException {
        List<Constraint> cons = new ArrayList<>();
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
        return result;
    }

    private Integer mapValueToConstraint(String paramValue, String type) throws IDLException {
        switch (ParameterType.valueOf(type.toUpperCase())) {
            case STRING:
            case ARRAY:
                return mapper.stringToInt(paramValue);
            case NUMBER:
            case INTEGER:
                return Integer.valueOf(paramValue);
            case BOOLEAN:
                return Boolean.parseBoolean(paramValue) ? 1 : 0;
            default:
                ExceptionManager.rethrow(LOG, ErrorType.ERROR_IN_PARAMETER_TYPE.toString() + " :" + type);
                return null;
        }
    }

    private String mapConstraintToValue(Integer intValue, String type) throws IDLException {
        switch (ParameterType.valueOf(type.toUpperCase())) {
            case STRING:
            case ARRAY:
                return mapper.getStringToIntMap().inverse().get(intValue);
            case NUMBER:
            case INTEGER:
                return intValue.toString();
            case BOOLEAN:
                return Boolean.toString(intValue == 1);
            default:
                ExceptionManager.rethrow(LOG, ErrorType.ERROR_IN_PARAMETER_TYPE.toString() + " :" + type);
                return null;
        }
    }


}
