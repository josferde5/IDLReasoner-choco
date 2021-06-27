package idlreasonerchoco.configuration.model;

public enum Paths {
	ROOT("/"),
	RESOURCES_PATH("src/main/resources/");
    
	private String name;
	
	private Paths(String name) {
		this.name = name;
	}
	
	public String toString() {
		return this.name;
	}
}
