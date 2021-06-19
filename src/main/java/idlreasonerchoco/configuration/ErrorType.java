package idlreasonerchoco.configuration;

public enum ErrorType {
	ERROR_READING_PROPERTIES("Error reading the properties"),
	ERROR_CREATING_FILE("Error creating file"),
	ERROR_WRITING_FILE("Error writing file");
	
	private String msg;
	
	private ErrorType(String msg) {
		this.msg = msg;
	}
	
	public String toString() {
		return this.msg;
	}
}
