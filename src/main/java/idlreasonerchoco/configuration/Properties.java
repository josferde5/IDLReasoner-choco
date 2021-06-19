package idlreasonerchoco.configuration;

public enum Properties {
	SOLVER("solver"),
    IDL_FILES_FOLDER("idlFolder"),
    MAX_RESULTS("maxResults"),
    TIMEOUT("timeout");
    
	private String name;
	
	private Properties(String name) {
		this.name = name;
	}
	
	public String toString() {
		return this.name;
	}
}
