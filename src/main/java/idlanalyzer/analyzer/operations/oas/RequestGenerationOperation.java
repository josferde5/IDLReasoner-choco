package idlanalyzer.analyzer.operations.oas;

import java.util.Map;

import idlanalyzer.configuration.IDLException;

/**
 * An interface for request generation, This interface has only
 * {@link #generate()} method which is implemented in OASRandomRequest class.
 * 
 * @see OASRandomRequest
 */
public interface RequestGenerationOperation {

	/**
	 * Return a valid random request if the parameter <code>valid</code> is set to
	 * true, otherwise invalid random request.
	 * 
	 * @return valid random request if the parameter <code>valid</code> is set to
	 *         true, otherwise invalid random request.
	 * @throws IDLException IDL exception.
	 */
	Map<String, String> generate() throws IDLException;
}
