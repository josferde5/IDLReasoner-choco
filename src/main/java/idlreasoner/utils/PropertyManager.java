package idlreasoner.utils;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class PropertyManager {
	
	private final static Logger LOG = Logger.getLogger(PropertyManager.class);
	
	private static Properties properties = null;

	public static void chargeProperties() {
		try {
			PropertyConfigurator.configure(Path.RESOURCES_PATH + "log4j.properties");
			
			properties = new Properties();
			properties.load(new FileInputStream(Path.RESOURCES_PATH + "idl-reasoner.properties"));
		} catch (Exception e) {
			LOG.error(ErrorType.ERROR_READING_PROPERTIES, e);
		}
	}
	
	public static Properties getProperties() {
		if (properties == null) {
			chargeProperties();
		}
		return properties;
	}

	public static void main(String... args) {
		PropertyManager.getProperties();
	}
}