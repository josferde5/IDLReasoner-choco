package idlreasonerchoco.configuration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import idlreasonerchoco.configuration.model.ErrorType;
import idlreasonerchoco.configuration.model.Files;
import idlreasonerchoco.configuration.model.IDLException;
import idlreasonerchoco.configuration.model.Paths;
import idlreasonerchoco.configuration.model.PropertiesType;
import idlreasonerchoco.utils.ExceptionManager;

public class IDLConfiguration {

    private static final Logger LOG = Logger.getLogger(IDLConfiguration.class);

    
    private final String specificationType;
    private final String operationPath;
    private final String operationType;
    private String apiSpecification;
    private Properties properties;

	public IDLConfiguration(String specificationType, String apiSpecification, String operationPath,
			String operationType, boolean chargeFromFile, String properties) throws IDLException {
		this.specificationType = specificationType;
		this.operationPath = operationPath;
		this.operationType = operationType;
		PropertyConfigurator.configure(Paths.RESOURCES_PATH.toString() + Files.LOG4J_PROPERTIES);
		chargeProperties(properties);	
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

    public void chargeProperties(String properties) {
        try (InputStream input = new ByteArrayInputStream(properties.getBytes())) {
        	Properties props = new Properties();
            props.load(input);
            this.properties = props;
        } catch (Exception e) {
            ExceptionManager.log(LOG, ErrorType.ERROR_READING_PROPERTIES.toString(), e);
            Properties props = new Properties();
            props.setProperty(PropertiesType.SOLVER.toString(), PropertiesType.SOLVER.getDefaultValue());
            props.setProperty(PropertiesType.MAX_RESULTS.toString(), PropertiesType.MAX_RESULTS.getDefaultValue());
            props.setProperty(PropertiesType.TIMEOUT.toString(), PropertiesType.TIMEOUT.getDefaultValue());
            this.properties = props;
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