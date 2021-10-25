package idlanalyzer.analyzer;

import java.util.List;
import java.util.Map;

import idlanalyzer.configuration.IDLConfiguration;
import idlanalyzer.configuration.IDLException;

/**
 * The Analyzer is an abstract class that provides analysis operations for IDL
 * and API requests.
 * 
 * 
 * This class provides the following operations:
 * <ul>
 * <li>{@link #isConsistent()}</li>
 * <li>{@link #isDeadParameter(String)}</li>
 * <li>{@link #isFalseOptional(String)}</li>
 * <li>{@link #isValidIDL()}</li>
 * <li>{@link #isValidRequest(Map)}</li>
 * <li>{@link #isValidPartialRequest(Map)}</li>
 * </ul>
 * 
 * 
 * 
 * Also, it can be used to {@link #getRandomValidRequest()},
 * {@link #getRandomInvalidRequest()}, and {@link #updateData(Map)}.
 * 
 */
public abstract class Analyzer {

	/**
	 * IDL configuration object.
	 * 
	 * @see IDLConfiguration
	 */
	protected final IDLConfiguration configuration;

	/**
	 * This constructor pass the received parameters to
	 * {@link #Analyzer(String, String, String, String, boolean)} to initialize
	 * IDLConfiguration instance.
	 * 
	 * @param specificationType specification type.
	 * @param apiSpecification  API specification.
	 * @param operationPath     operation path.
	 * @param operationType     operation type.
	 * 
	 * @see IDLConfiguration
	 * 
	 * @throws IDLException IDL exception.
	 */
	protected Analyzer(String specificationType, String apiSpecification, String operationPath, String operationType)
			throws IDLException {
		this(specificationType, apiSpecification, operationPath, operationType, false);
	}

	/**
	 * This constructor uses the received parameters to initialize IDLConfiguration
	 * instance.
	 * 
	 * @param specificationType specification type.
	 * @param apiSpecification  API specification.
	 * @param operationPath     operation path.
	 * @param operationType     operation type.
	 * @param specAsString true or false.
	 * @see IDLConfiguration
	 * 
	 * @throws IDLException IDL exception.
	 */
	protected Analyzer(String specificationType, String apiSpecification, String operationPath, String operationType,
			boolean specAsString) throws IDLException {
		this.configuration = new IDLConfiguration(specificationType, apiSpecification, operationPath, operationType,
				specAsString);
	}

	/**
	 * Returns true if the IDL is consistent. An IDL is consistent if there exists at
	 * least one request satisfying all the dependencies of the specification.
	 * 
	 * @return true if the IDL is consistent, otherwise false.
	 * @throws IDLException IDL exception.
	 */
	public abstract boolean isConsistent() throws IDLException;

	/**
	 * Returns true if the parameter is dead. A parameter is dead if it cannot be
	 * included in any valid call to the service.
	 * 
	 * @param paramName parameter name.
	 * @return true if the parameter is dead, otherwise false.
	 * @throws IDLException IDL exception.
	 */
	public abstract boolean isDeadParameter(String paramName) throws IDLException;

	/**
	 * Returns true if the parameter is a false optional. A parameter is false
	 * optional if it is required despite being defined as optional.
	 * 
	 * @param paramName parameter name.
	 * @return true if the parameter is false optional, otherwise false.
	 * @throws IDLException IDL exception.
	 */
	public abstract boolean isFalseOptional(String paramName) throws IDLException;

	/**
	 * Returns true if the IDL is valid. An IDL specification is valid if it is
	 * consistent and it does not contain any dead or false optional parameters.
	 * 
	 * @return true if the IDL is valid, otherwise false.
	 * @see idlanalyzer.analyzer.operations.oas.OASConsistent
	 * @see idlanalyzer.analyzer.operations.oas.OASDeadParameter
	 * @see idlanalyzer.analyzer.operations.oas.OASFalseOptional
	 * @throws IDLException IDL exception.
	 */
	public abstract Boolean isValidIDL() throws IDLException;

	/**
	 * Returns a map of random valid request. This map contains parameter name as
	 * a key and parameter's value as a value.
	 * 
	 * @return valid request.
	 *         <ul>
	 *         <li><b>key</b> : parameter name</li>
	 *         <li><b>value</b> : parameter value</li>
	 *         </ul>
	 * @throws IDLException IDL exception.
	 */
	public abstract Map<String, String> getRandomValidRequest() throws IDLException;

	/**
	 * Returns a map of random invalid request. This map contains parameter name as
	 * a key and parameter's value as a value.
	 * 
	 * @return invalid request.
	 *         <ul>
	 *         <li><b>key</b> : parameter name</li>
	 *         <li><b>value</b> : parameter value</li>
	 *         </ul>
	 * @throws IDLException IDL exception.
	 */
	public abstract Map<String, String> getRandomInvalidRequest() throws IDLException;

	/**
	 * Returns true if the request is valid. A request is valid if it satisfies all
	 * the dependencies of the IDL specification.
	 * 
	 * @param request to be checked if it is valid or not.
	 * @return true if the request is valid, otherwise false.
	 * @throws IDLException IDL exception.
	 */
	public abstract boolean isValidRequest(Map<String, String> request) throws IDLException;

	/**
	 * Returns true if the request is partially valid. A request is partially valid
	 * means that some other parameters should still be included to make it a full
	 * valid request.
	 * 
	 * @param request to be checked if it is partially valid or not.
	 * @return true if the request is partially valid, otherwise false.
	 * @throws IDLException IDL exception.
	 */
	public abstract boolean isValidPartialRequest(Map<String, String> request) throws IDLException;

	/**
	 * Updates parameters data as a map. This map contains parameter name as key and
	 * list of parameter values as value.
	 * 
	 * <ul>
	 * <li><b>key</b> : parameter name</li>
	 * <li><b>value</b> : list of values</li>
	 * </ul>
	 * 
	 * @param data to be updated
	 * @throws IDLException IDL exception.
	 */
	public abstract void updateData(Map<String, List<String>> data) throws IDLException;

}