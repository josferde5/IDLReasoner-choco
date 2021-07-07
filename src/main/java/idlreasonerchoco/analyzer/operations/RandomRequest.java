package idlreasonerchoco.analyzer.operations;

import idlreasonerchoco.configuration.model.ErrorType;
import idlreasonerchoco.configuration.model.IDLException;
import idlreasonerchoco.mapper.Mapper;
import idlreasonerchoco.model.ParameterType;
import idlreasonerchoco.utils.ExceptionManager;
import idlreasonerchoco.utils.Utils;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.apache.log4j.Logger;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class RandomRequest implements RequestGenerationOperation {

    private static final Logger LOG = Logger.getLogger(RandomRequest.class);

    private final Mapper mapper;
    private final boolean valid;

    public RandomRequest(Mapper mapper, boolean valid) {
        this.mapper = mapper;
        this.valid = valid;
    }

    public Map<String, String> generate() throws IDLException {
        if (valid) {
            mapper.getChocoModel().getSolver().reset();
            return mapRequest();
        } else {
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
    }

    private Map<String, String> mapRequest() throws IDLException {
        Map<String, String> request = null;
        Consistent consistent = new Consistent(mapper);
        if (consistent.analyze()) {
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
