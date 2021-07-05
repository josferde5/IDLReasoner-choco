package idlreasonerchoco.configuration.model;

public enum PropertiesType {
	SOLVER("solver", "Gecode"),
    MAX_RESULTS("maxResults", "100"),
    TIMEOUT("timeout", "1000");
    
	private String name;
	private String defaultValue;
	
	PropertiesType(String name, String defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	public String getDefaultValue() {
		return this.defaultValue;
	}
}