package idlreasonerchoco.configuration;

public class IDLException extends Exception {
	private static final long serialVersionUID = 6228831805487458374L;
	
	public IDLException(String msg) {
		super(msg);
	}
	
	public IDLException(String msg, Throwable e) {
		super(msg, e);
	}
}
