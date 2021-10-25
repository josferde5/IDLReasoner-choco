package idlanalyzer.utils;

import org.apache.log4j.Logger;

import idlanalyzer.configuration.IDLException;

/**
 * ExceptionManager class manage exceptions. It logs errors and threw new IDL
 * exception.
 *
 */
public class ExceptionManager {

	private ExceptionManager() {
	}

	/**
	 * It calls {@link #log(Logger, String, Exception)} method and threw new IDL
	 * exception.
	 * 
	 * @param logger    logger object.
	 * @param error     error message.
	 * @param exception exception object.
	 * @throws IDLException IDL exception.
	 */
	public static void rethrow(Logger logger, String error, Exception exception) throws IDLException {
		log(logger, error, exception);
		throw new IDLException(error, exception);
	}

	/**
	 * It calls {@link #rethrow(Logger, String, Exception)} method.
	 * 
	 * @param logger logger object.
	 * @param error  error message.
	 * @throws IDLException IDL exception.
	 */
	public static void rethrow(Logger logger, String error) throws IDLException {
		rethrow(logger, error, null);
	}

	/**
	 * Log errors. It sets logger error with the error message and exception object.
	 * 
	 * @param logger    logger object.
	 * @param error     error message.
	 * @param exception exception object
	 */
	public static void log(Logger logger, String error, Exception exception) {
		logger.error(error, exception);
	}

	/**
	 * It calls {@link #log(Logger, String, Exception)} method.
	 * 
	 * @param logger logger object.
	 * @param error  error message.
	 */
	public static void log(Logger logger, String error) {
		log(logger, error, null);
	}
}
