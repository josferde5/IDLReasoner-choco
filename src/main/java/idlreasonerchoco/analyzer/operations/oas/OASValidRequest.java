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
        List<Constraint> cons = new ArrayList<>();
        if (request.keySet().stream().allMatch(param -> mapper.getVariablesMap().containsKey(Utils.parseIDLParamName(param)))) {

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
        } else {
            ExceptionManager.rethrow(LOG, ErrorType.ERROR_UNKNOWN_PARAM_IN_REQUEST.toString());
            return false;
        }
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
}
