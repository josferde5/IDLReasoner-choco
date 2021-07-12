package idlreasonerchoco.configuration;

public class IDLConfiguration {
    
    private final String specificationType;
    private final String operationPath;
    private final String operationType;
    private String apiSpecification;
    private boolean specAsString;

	public IDLConfiguration(String specificationType, String apiSpecification, String operationPath,
			String operationType, boolean specAsString) {
		this.specificationType = specificationType;
		this.operationPath = operationPath;
		this.operationType = operationType;
		this.apiSpecification = apiSpecification;
		this.specAsString = specAsString;
	}

    public String getSpecificationType() {
        return specificationType;
    }
    
    public String getApiSpecification() {
        return apiSpecification;
    }

    public String getOperationPath() {
        return operationPath;
    }

    public String getOperationType() {
        return operationType;
    }

    public boolean isSpecAsString() {
        return specAsString;
    }
}