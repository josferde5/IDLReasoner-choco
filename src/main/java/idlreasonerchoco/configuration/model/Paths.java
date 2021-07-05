package idlreasonerchoco.configuration.model;

public enum Paths {
	ROOT("/"),
	RESOURCES_PATH("src/main/resources/");
    
	private String name;
	
	Paths(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
