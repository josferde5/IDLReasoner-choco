package idlreasonerchoco.utils;

import org.apache.log4j.Logger;

import idlreasonerchoco.configuration.model.IDLException;

public class ExceptionManager {

	public static void rethrow(Logger logger, String error, Exception exception) throws IDLException {
		log(logger, error.toString(), exception);
		throw new IDLException(error.toString(), exception);
	}
	
	public static void rethrow(Logger logger, String error) throws IDLException {
		rethrow(logger, error, null);
	}
	
	public static void log(Logger logger, String error, Exception exception) {
		logger.error(error.toString(), exception);
	}
	
	public static void log(Logger logger, String error) {
		log(logger, error, null);
	}
}
