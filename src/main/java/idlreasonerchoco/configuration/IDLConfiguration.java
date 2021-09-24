package idlreasonerchoco.configuration;

/**
 * IDLConfiguration setups IDL configurations. It configures specification type,
 * API specification, operation path, and operation type.
 */
public class IDLConfiguration {

	/**
	 * Specification type.
	 */
	private final String specificationType;

	/**
	 * Operation path.
	 */
	private final String operationPath;

	/**
	 * Operation type.
	 */
	private final String operationType;

	/**
	 * API specification.
	 */
	private String apiSpecification;

	/**
	 * 
	 */
	private boolean specAsString;

	/**
	 * Constructs a new IDLConfiguration object, and initializes all the required
	 * configurations.
	 * 
	 * @param specificationType specification type.
	 * @param apiSpecification API specification.
	 * @param operationPath operation path.
	 * @param operationType operation type.
	 * @param specAsString true or false.
	 */
	public IDLConfiguration(String specificationType, String apiSpecification, String operationPath,
			String operationType, boolean specAsString) {
		this.specificationType = specificationType;
		this.operationPath = operationPath;
		this.operationType = operationType;
		this.apiSpecification = apiSpecification;
		this.specAsString = specAsString;
	}

	/**
	 * Returns specification type.
	 * 
	 * @return specification type.
	 */
	public String getSpecificationType() {
		return specificationType;
	}

	/**
	 * Returns API specification.
	 * 
	 * @return API specification
	 */
	public String getApiSpecification() {
		return apiSpecification;
	}

	/**
	 * Returns operation path.
	 * 
	 * @return operation path.
	 */
	public String getOperationPath() {
		return operationPath;
	}

	/**
	 * Returns operation type.
	 * 
	 * @return operation type.
	 */
	public String getOperationType() {
		return operationType;
	}

	public boolean isSpecAsString() {
		return specAsString;
	}
}