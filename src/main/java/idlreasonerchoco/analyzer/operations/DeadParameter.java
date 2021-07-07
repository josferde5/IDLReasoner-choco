package idlreasonerchoco.analyzer.operations;

import idlreasonerchoco.configuration.model.ErrorType;
import idlreasonerchoco.configuration.model.IDLException;
import idlreasonerchoco.mapper.Mapper;
import idlreasonerchoco.utils.ExceptionManager;
import idlreasonerchoco.utils.Utils;
import org.apache.log4j.Logger;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.BoolVar;

public class DeadParameter implements AnalysisOperation {

    private static final Logger LOG = Logger.getLogger(DeadParameter.class);

    private final Mapper mapper;
    private final String paramName;

    public DeadParameter(Mapper mapper, String paramName) {
        this.mapper = mapper;
        this.paramName = paramName;
    }

    public boolean analyze() throws IDLException {
        if (mapper.getVariablesMap().get(Utils.parseIDLParamName(paramName) + "Set") != null) {
            BoolVar varSet = mapper.getVariablesMap().get(Utils.parseIDLParamName(paramName) + "Set").asBoolVar();
            Constraint cons = mapper.getChocoModel().arithm(varSet, "=", 1);
            cons.post();
            boolean result = !mapper.getChocoModel().getSolver().solve();
            mapper.getChocoModel().unpost(cons);
            return result;
        } else {
            ExceptionManager.rethrow(LOG, ErrorType.ERROR_OPERATION_PARAM.toString());
            return false;
        }
    }
}
