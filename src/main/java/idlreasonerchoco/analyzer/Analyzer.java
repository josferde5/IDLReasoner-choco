package idlreasonerchoco.analyzer;

import java.util.Map;

import org.apache.log4j.Logger;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.BoolVar;

import idlreasonerchoco.configuration.IDLConfiguration;
import idlreasonerchoco.configuration.model.IDLException;
import idlreasonerchoco.mapper.Mapper;
import idlreasonerchoco.utils.Utils;

public class Analyzer {

	private static final Logger LOG = Logger.getLogger(Analyzer.class);
	
	private final Mapper mapper;
	private final IDLConfiguration configuration;
	
	public Analyzer(String specificationType, String apiSpecification, String operationPath, String operationType) throws IDLException {
		this(specificationType, apiSpecification, operationPath, operationType, true, null);
	}
	
	public Analyzer(String specificationType, String apiSpecification, String operationPath, String operationType, boolean chargeFromFile, String idlProperties) throws IDLException {
		this.configuration = new IDLConfiguration(specificationType, apiSpecification, operationPath, operationType, chargeFromFile, idlProperties);
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

	public Map<String, String> getRandomRequest() {
		return null;
	}
	
	public boolean isValidRequest() {
		return false;
	}
	
	public boolean isValidPartialRequest() {
		return false;
	}
	
}
