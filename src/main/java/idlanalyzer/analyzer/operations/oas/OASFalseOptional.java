package idlanalyzer.analyzer.operations.oas;

import org.apache.log4j.Logger;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.BoolVar;

import idlanalyzer.configuration.ErrorType;
import idlanalyzer.configuration.IDLException;
import idlanalyzer.mapper.OASMapper;
import idlanalyzer.utils.ExceptionManager;
import idlanalyzer.utils.Utils;

/**
 * This class implements AnalysisOperation. It uses {@link #analyze()} to check if
 * the parameter is false optional.
 */
public class OASFalseOptional implements AnalysisOperation {

	private static final Logger LOG = Logger.getLogger(OASFalseOptional.class);

	/**
	 * OASMapper object.
	 * @see OASMapper
	 */
	private final OASMapper mapper;
	
	/**
	 * Parameter name.
	 */
	private final String paramName;

	/**
	 * Creates OASFalseOptional object.
	 * 
	 * @param mapper OASMapper object.
	 * @param paramName parameter name.
	 * @see OASMapper
	 */
	public OASFalseOptional(OASMapper mapper, String paramName) {
		this.mapper = mapper;
		this.paramName = paramName;
	}

	/**
	 * Returns true if the parameter is false optional. The parameter is false
	 * optional, if there is no solutions after removing the parameter from a
	 * consistent model.
	 * 
	 * @return true if the parameter is false optional, otherwise false.
	 * 
	 */
	public boolean analyze() throws IDLException {
		if (mapper.getParameters().stream().anyMatch(x -> x.getName().equals(paramName) && x.getRequired())) {
			return false;
		}
		if (mapper.getVariablesMap().get(Utils.parseIDLParamName(paramName) + "Set") != null) {
			boolean consistent = new OASConsistent(mapper).analyze();
			mapper.getChocoModel().getSolver().reset();
			BoolVar varSet = mapper.getVariablesMap().get(Utils.parseIDLParamName(paramName) + "Set").asBoolVar();
			Constraint cons = mapper.getChocoModel().arithm(varSet, "=", 0);
			cons.post();
			boolean result = consistent && !mapper.getChocoModel().getSolver().solve();
			mapper.getChocoModel().unpost(cons);
			mapper.getChocoModel().getSolver().reset();
			return result;
		} else {
			ExceptionManager.rethrow(LOG, ErrorType.ERROR_OPERATION_PARAM.toString());
			return false;
		}
	}
}
