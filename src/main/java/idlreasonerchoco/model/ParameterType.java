package idlreasonerchoco.model;

public enum ParameterType {
	NUMBER("number"),
	ARRAY("array"),
	INTEGER("integer"),
	STRING("string"),
	BOOLEAN("boolean");
	
	private String type;
	
	ParameterType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type;
	}
}
