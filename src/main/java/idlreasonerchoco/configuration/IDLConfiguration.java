package idlreasonerchoco.configuration;

import static idlreasonerchoco.utils.FileManager.appendContentToFile;
import static idlreasonerchoco.utils.FileManager.createFileIfNotExists;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import idlreasonerchoco.configuration.model.ErrorType;
import idlreasonerchoco.configuration.model.Files;
import idlreasonerchoco.configuration.model.IDLException;
import idlreasonerchoco.configuration.model.Paths;
import idlreasonerchoco.utils.ExceptionManager;

public class IDLConfiguration {

	private final static Logger LOG = Logger.getLogger(IDLConfiguration.class);

	private final Properties properties;
	private final Paths paths;
	private final String specificationType;
	private final String idlPath;
	private final String apiSpecificationPath;
	private final String operationPath;
	private final String operationType;

	public Properties chargeProperties() throws IDLException {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(paths.RESOURCES_PATH + Files.IDL_REASONER_PROPERTIES));

			return properties;
		} catch (Exception e) {
			ExceptionManager.rethrow(LOG, ErrorType.ERROR_READING_PROPERTIES.toString(), e);
			return null;
		}
	}

	public IDLConfiguration(String specificationType, String idlPath, String apiSpecificationPath, String operationPath,
			String operationType) throws IDLException {
		this.specificationType = specificationType;
		this.idlPath = idlPath;
		this.apiSpecificationPath = apiSpecificationPath;
		this.operationPath = operationPath;
		this.operationType = operationType;
		this.paths = new Paths();

		PropertyConfigurator.configure(paths.RESOURCES_PATH + Files.LOG4J_PROPERTIES);
		this.properties = chargeProperties();
	}

	public Properties getProperties() {
		return properties;
	}

	public Paths getPaths() {
		return paths;
	}

	public String getSpecificationType() {
		return specificationType;
	}

	public String getIdlPath() {
		return idlPath;
	}

	public String getApiSpecificationPath() {
		return apiSpecificationPath;
	}

	public String getOperationPath() {
		return operationPath;
	}

	public String getOperationType() {
		return operationType;
	}
}