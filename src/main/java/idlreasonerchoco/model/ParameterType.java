package idlreasonerchoco.model;

/**
 * This enum represents parameter types.
 */
public enum ParameterType {
	NUMBER("number"),
	ARRAY("array"),
	INTEGER("integer"),
	STRING("string"),
	BOOLEAN("boolean");
	
	/**
	 * parameter type.
	 */
	private String type;
	
	ParameterType(String type) {
		this.type = type;
	}

	/**
	 * Returns parameter type.
	 * 
	 * @return parameter type.
	 */
	@Override
	public String toString() {
		return this.type;
	}
}
