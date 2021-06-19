package idlreasonerchoco.configuration;

public enum ErrorType {
	ERROR_READING_PROPERTIES("Error reading the properties: ");
	
	private String msg;
	
	private ErrorType(String msg) {
		this.msg = msg;
	}
	
	public String toString() {
		return this.msg;
	}
}
