package idlreasonerchoco.configuration;

import java.util.Properties;

import org.apache.log4j.Logger;

import idlreasonerchoco.configuration.model.ErrorType;
import idlreasonerchoco.configuration.model.IDLException;
import idlreasonerchoco.utils.ExceptionManager;

public class IDLConfiguration {

    private static final Logger LOG = Logger.getLogger(IDLConfiguration.class);

    
    private final String specificationType;
    private final String operationPath;
    private final String operationType;
    private String apiSpecification;
    private Properties properties;

	public IDLConfiguration(String specificationType, String apiSpecification, String operationPath,
			String operationType, boolean chargeFromFile) throws IDLException {
		this.specificationType = specificationType;
		this.operationPath = operationPath;
		this.operationType = operationType;
		chargeSpecification(apiSpecification, chargeFromFile);
	}

	private void chargeSpecification(String apiSpecification, boolean chargeFromFile) throws IDLException {
		if(chargeFromFile) {
			try {
				this.apiSpecification = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(apiSpecification)));
	        } catch (Exception e) {
	            ExceptionManager.rethrow(LOG, ErrorType.ERROR_READING_SPECIFICATION.toString(), e);
	        }
		} else {
		    this.apiSpecification = apiSpecification;
        }
	}
	
	public Properties getProperties() {
        return properties;
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
}