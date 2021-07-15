package idlreasonerchoco.configuration;

public enum ErrorType {
	ERROR_READING_PROPERTIES("Error reading the properties, using default values"),
	ERROR_CREATING_FILE("Error creating file"),
	ERROR_READING_DEPENDECIES("Error reading dependecies from specification"),
	BAD_OAS_OPERATION("Bad OAS operation type"),
	BAD_SPECIFICATION("Specification type not supported"),
	ERROR_WRITING_FILE("Error writing file"), 
	ERROR_READING_SPECIFICATION("Error reading API specification"), 
	ERROR_MAPPING_CONSTRAINTS_FROM_IDL("Error mapping constraints from IDL Mapper"),
	ERROR_OPERATION_PATH("The operation path does not exist"),
	ERROR_OPERATION_PARAM("The parameter does not exist in the specification"),
	ERROR_UNKNOWN_PARAM_IN_REQUEST("The request contains unknown parameters"),
	ERROR_IN_PARAMETER_TYPE("The parameter type is not allowed for IDLReasoner to work");
	
	private String msg;
	
	ErrorType(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return this.msg;
	}
}
