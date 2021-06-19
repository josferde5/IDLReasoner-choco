package idlreasoner.utils;

public enum Path {
	RESOURCES_PATH("src/main/resources/");
	
	private String path;
	
	private Path(String path) {
		this.path = path;
	}
	
	public String toString() {
		return this.path;
	}
}
