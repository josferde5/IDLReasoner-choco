package idlreasonerchoco.utils;

import es.us.isa.idl.generator.ReservedWords;

/**
 * This class is used to provide utility methods for example
 * {@link #parseIDLParamName(String)}.
 */
public class Utils {

	private Utils() {
	}

	/**
	 * Parse parameter name.
	 * 
	 * It removes special characters from parameters name. Also, if the parameter is
	 * a reserved keyword, it adds _R to the parameter.
	 * 
	 * @param paramName parameter name to be parsed.
	 * @return parsed parameter name.
	 */
	public static String parseIDLParamName(String paramName) {
		String parsedParamName = paramName.replaceAll("^\\[|\\]$", "").replaceAll("[\\.\\-\\/\\:\\[\\]]", "_");
		if (ReservedWords.RESERVED_WORDS.contains(parsedParamName))
			parsedParamName += "_R";
		return parsedParamName;
	}
}
