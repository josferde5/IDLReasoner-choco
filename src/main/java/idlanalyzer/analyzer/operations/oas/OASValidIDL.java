package idlanalyzer.analyzer.operations.oas;

import idlanalyzer.configuration.IDLException;
import idlanalyzer.mapper.OASMapper;
import io.swagger.v3.oas.models.parameters.Parameter;

/**
 * This class implements AnalysisOperation. It uses {@link #analyze()} to check
 * if the IDL is valid or not.
 */
public class OASValidIDL implements AnalysisOperation {

	/**
	 * OASMapper object.
	 * @see OASMapper
	 */
	private final OASMapper mapper;

	/**
	 * Creates OASValidIDL object.
	 * 
	 * @param mapper OASMapper object.
	 * @see OASMapper
	 */
	public OASValidIDL(OASMapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * Returns true if the IDL is valid. An IDL specification is valid if it is
	 * consistent and it does not contain any dead or false optional parameters.
	 * 
	 * @return true if the IDL is valid, otherwise false.
	 */
	public boolean analyze() throws IDLException {
		OASConsistent consistent = new OASConsistent(mapper);
		boolean result = consistent.analyze();
		for (Parameter p : mapper.getParameters()) {
			OASDeadParameter deadParameter = new OASDeadParameter(mapper, p.getName());
			OASFalseOptional falseOptional = new OASFalseOptional(mapper, p.getName());
			result &= !deadParameter.analyze() && !falseOptional.analyze();
			if (!result) {
				break;
			}
		}
		return result;
	}

}
