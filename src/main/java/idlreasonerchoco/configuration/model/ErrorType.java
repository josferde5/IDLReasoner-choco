package idlreasonerchoco.configuration.model;

public enum ErrorType {
	ERROR_READING_PROPERTIES("Error reading the properties, using default values"),
	ERROR_CREATING_FILE("Error creating file"),
	ERROR_READING_DEPENDECIES("Error reading dependecies from specification"),
	BAD_OAS_OPERATION("Bad OAS operation type"),
	BAD_SPECIFICATION("Specification type not supported"),
	ERROR_WRITING_FILE("Error writing file"), 
	ERROR_READING_SPECIFICATION("Error reading API specification"), 
	ERROR_MAPPING_CONSTRAINTS_FROM_IDL("Error mapping constraints from IDL Mapper");
	
	private String msg;
	
	private ErrorType(String msg) {
		this.msg = msg;
	}
	
	public String toString() {
		return this.msg;
	}
}
