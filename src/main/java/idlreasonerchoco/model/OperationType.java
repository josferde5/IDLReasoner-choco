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
	
	OperationType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type;
	}
}
