package idlanalyzer.mapper;

import idlanalyzer.configuration.IDLConfiguration;
import idlanalyzer.configuration.IDLException;

/**
 * An Abstract class that initializes configuration object and based on that it
 * reads and generates IDL from open API specification.
 *
 */
public abstract class Mapper {

	/**
	 * IDLConfiguration object.
	 */
	protected final IDLConfiguration configuration;

	/**
	 * Creates Mapper object.
	 * 
	 * @param configuration IDLConfiguration object.
	 * @throws IDLException IDL exception.
	 */
	protected Mapper(IDLConfiguration configuration) throws IDLException {
		this.configuration = configuration;
	}

}
