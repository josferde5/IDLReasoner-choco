package idlanalyzer.model;

/**
 * This enum represents operation types.
 */
public enum OperationType {
	GET("get"), DELETE("delete"), POST("post"), PUT("put"), PATCH("patch"), HEAD("head"), OPTIONS("options");

	/**
	 * Operation type.
	 */
	private String type;

	OperationType(String type) {
		this.type = type;
	}

	/**
	 * Returns operation type. Operation type: get, delete, post, put, patch, head,
	 * options.
	 * 
	 * @return operation type.
	 */
	@Override
	public String toString() {
		return this.type;
	}
}
