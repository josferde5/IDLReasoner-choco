package idlanalyzer.configuration;

/**
 * The IDLException class extends Exception class.
 * 
 * @see Exception
 */
public class IDLException extends Exception {
	private static final long serialVersionUID = 6228831805487458374L;
	
	/**
	 * Constructs a new IDL exception with the specified detail message.
     * 
	 * @param msg the detail message.
	 */
	public IDLException(String msg) {
		super(msg);
	}
	/**
	 * Constructs a new IDL exception with the specified detail message and
     * cause.
	 * @param msg the detail message.
	 * @param e the cause
	 */
	public IDLException(String msg, Throwable e) {
		super(msg, e);
	}
}
