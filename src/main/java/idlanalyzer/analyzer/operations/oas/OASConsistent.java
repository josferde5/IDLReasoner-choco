package idlanalyzer.analyzer.operations.oas;

import idlanalyzer.configuration.IDLException;
import idlanalyzer.mapper.OASMapper;

/**
 * This class implements AnalysisOperation. It uses {@link #analyze()} to check
 * if the IDL is consistent or not.
 */
public class OASConsistent implements AnalysisOperation {

	/**
	 * OASMapper object.
	 * 
	 * @see OASMapper
	 */
	private final OASMapper mapper;

	/**
	 * Creates an OASConsistent object.
	 * 
	 * @param mapper OASMapper object.
	 * @see OASMapper
	 */
	public OASConsistent(OASMapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * Returns true if the IDL is consistent. An IDL is consistent if the model has
	 * a solution.
	 * 
	 * @return true if the model has a solution, otherwise false.
	 * 
	 */
	public boolean analyze() {
		return mapper.getChocoModel().getSolver().solve();
	}
}
