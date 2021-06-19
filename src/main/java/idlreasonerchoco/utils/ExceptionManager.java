package idlreasonerchoco.utils;

import org.apache.log4j.Logger;

import idlreasonerchoco.configuration.IDLException;

public class ExceptionManager {

	public static void rethrow(Logger logger, String error, Exception exception) throws IDLException {
		log(logger, error.toString(), exception);
		throw new IDLException(error.toString(), exception);
	}
	
	public static void log(Logger logger, String error, Exception exception) {
		logger.error(error.toString(), exception);
	}
}
