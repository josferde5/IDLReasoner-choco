package idlanalyzer.analyzer.operations.oas;

import idlanalyzer.configuration.IDLException;

/**
 * An interface for analysis operations, This interface has only
 * {@link #analyze()} method which is implemented in following classes:
 * 
 * <ul>
 * <li>{@link OASConsistent}</li>
 * <li>{@link OASDeadParameter}</li>
 * <li>{@link OASFalseOptional}</li>
 * <li>{@link OASValidIDL}</li>
 * <li>{@link OASValidRequest}</li>
 * </ul>
 * 
 * @see OASConsistent
 * @see OASDeadParameter
 * @see OASFalseOptional
 * @see OASValidIDL
 * @see OASValidRequest
 */
public interface AnalysisOperation {

	/**
	 * This method analyzes the IDL and API requests. It is implemented in following
	 * classes:
	 * 
	 * <ul>
	 * <li>{@link OASConsistent}</li>
	 * <li>{@link OASDeadParameter}</li>
	 * <li>{@link OASFalseOptional}</li>
	 * <li>{@link OASValidIDL}</li>
	 * <li>{@link OASValidRequest}</li>
	 * </ul>
	 * 
	 * @return
	 *         <ul>
	 *         <li>true if the IDL is consistent, otherwise false.</li>
	 *         <li>true if the parameter is dead, otherwise false.</li>
	 *         <li>true if the parameter is false optional, otherwise false.</li>
	 *         <li>true if the IDL is valid, otherwise false.</li>
	 *         <li>true if the request/partial request is valid, otherwise
	 *         false.</li>
	 *         </ul>
	 * @see OASConsistent
	 * @see OASDeadParameter
	 * @see OASFalseOptional
	 * @see OASValidIDL
	 * @see OASValidRequest
	 * @throws IDLException IDL exception.
	 */
	boolean analyze() throws IDLException;
}
