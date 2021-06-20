package idlreasonerchoco.model;

public enum OperationType {
	GET("get"),
	DELETE("delete"),
	POST("post"),
	PUT("put"),
	PATCH("patch"),
	HEAD("head"),
	OPTIONS("options");
	
	private String type;
	
	private OperationType(String type) {
		this.type = type;
	}
	
	public String toString() {
		return this.type;
	}
}
