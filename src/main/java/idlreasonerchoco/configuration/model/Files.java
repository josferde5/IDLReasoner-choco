package idlreasonerchoco.configuration.model;

public enum Files {
    BASE_CONSTRAINTS_FILE("base_constraints.mzn"),
    BASE_DATA_FILE("base_data.dzn"),
    DATA_FILE("data.dzn"),
    IDL_AUX_FILE("constraints.idl"),
    STRING_INT_MAPPING_FILE("string_int_mapping.json"),
	LOG4J_PROPERTIES("log4j2.properties"),
	IDL_REASONER_PROPERTIES("idl-reasoner.properties");
    
	private String name;
	
	private Files(String name) {
		this.name = name;
	}
	
	public String toString() {
		return this.name;
	}
}
