package idlreasonerchoco.configuration.model;

public enum Files {
	LOG4J_PROPERTIES("log4j2.properties");
    
	private String name;
	
	private Files(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
