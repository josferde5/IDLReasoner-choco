package idlreasonerchoco.analyzer;

import java.util.Map;

import org.apache.log4j.Logger;

import idlreasonerchoco.configuration.IDLConfiguration;
import idlreasonerchoco.configuration.model.IDLException;
import idlreasonerchoco.mapper.Mapper;
import idlreasonerchoco.resolutor.Resolutor;

public class Analyzer {

	private final static Logger LOG = Logger.getLogger(Analyzer.class);
	
	private final Mapper mapper;
	private final Resolutor resolutor;
	private final IDLConfiguration configuration;
	
	private boolean lastRandomReqWasValid;
	private boolean needReloadConstraintsFile;
	
	//TODO constructor que reciba la especificación por parámetros y no el path
	public Analyzer(String specificationType, String idlPath, String apiSpecificationPath, String operationPath, String operationType) throws IDLException {
		this.configuration = new IDLConfiguration(specificationType, idlPath, apiSpecificationPath, operationPath, operationType);
		this.needReloadConstraintsFile = true;
		this.lastRandomReqWasValid = false;
		this.resolutor = new Resolutor(configuration);
		this.mapper = new Mapper(configuration);
	}
	
	public Boolean isValidRequest() {
		return null;
	}
	
	public Boolean isConsistent() {
		return null;
	}
	
	public Boolean isDeadParameter() {
		return null;
	}
	
	public Boolean isFalseOptional() {
		return null;
	}
	
	public Boolean isValidIDL() {
		return null;
	}
	
	public Map<String, String> getRandomRequest() {
		return null;
	}
}
