package idlreasonerchoco.configuration;

import static idlreasonerchoco.utils.FileManager.appendContentToFile;
import static idlreasonerchoco.utils.FileManager.createFileIfNotExists;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import idlreasonerchoco.utils.ExceptionManager;
import idlreasonerchoco.utils.FileManager;

public class IDLConfiguration {
	
	private final static Logger LOG = Logger.getLogger(IDLConfiguration.class);
	
	private final Properties properties;
	private final Paths paths;
	
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
	
    public void initFiles() throws IDLException {
    	createFileIfNotExists(this.paths.IDL_AUX_FOLDER + Files.STRING_INT_MAPPING_FILE);
        appendContentToFile(this.paths.IDL_AUX_FOLDER + Files.STRING_INT_MAPPING_FILE, "{ }");
        createFileIfNotExists(this.paths.IDL_AUX_FOLDER + Files.IDL_AUX_FILE);
        createFileIfNotExists(this.paths.IDL_AUX_FOLDER + Files.BASE_CONSTRAINTS_FILE);
        createFileIfNotExists(this.paths.IDL_AUX_FOLDER + Files.BASE_DATA_FILE);
        createFileIfNotExists(this.paths.IDL_AUX_FOLDER + Files.DATA_FILE);
    }
	
	public IDLConfiguration() throws IDLException {
		this.paths = new Paths();
		PropertyConfigurator.configure(paths.RESOURCES_PATH + Files.LOG4J_PROPERTIES);	
		this.properties = chargeProperties();
		initFiles();
	}

	public Properties getProperties() {
		return properties;
	}

	public Paths getPaths() {
		return paths;
	}
}