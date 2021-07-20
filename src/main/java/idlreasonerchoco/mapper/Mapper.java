package idlreasonerchoco.mapper;

import idlreasonerchoco.configuration.IDLConfiguration;
import idlreasonerchoco.configuration.IDLException;

public abstract class Mapper {
	
    protected final IDLConfiguration configuration;

    protected Mapper(IDLConfiguration configuration) throws IDLException {
        this.configuration = configuration;
    }

}
