package idlreasoner.analyzer;

import idlreasoner.mapper.Mapper;
import idlreasoner.model.APISpec;
import idlreasoner.model.IDLResponse;
import idlreasoner.model.IDLSpec;
import idlreasoner.model.Parameter;
import idlreasoner.model.Request;
import idlreasoner.resolutor.Resolutor;

public class Analyzer {

	private Mapper mapper;
	private Resolutor resolutor;
	
	public Analyzer(Mapper mapper, Resolutor resolutor) {
		this.mapper = mapper;
		this.resolutor = resolutor;
	}
	
	public Analyzer() {
		this.mapper = new Mapper();
		this.resolutor = new Resolutor();
	}
	
	public Boolean isValidRequest(IDLSpec idlSpecs, APISpec apiSpecs, Request request) {
		return null;
	}
	
	public Boolean isConsistent(IDLSpec idlSpecs, APISpec apiSpecs) {
		return null;
	}
	
	public Boolean isDeadParameter(IDLSpec idlSpecs, APISpec apiSpecs, Parameter request) {
		return null;
	}
	
	public Boolean isFalseOptional(IDLSpec idlSpecs, APISpec apiSpecs, Parameter request) {
		return null;
	}
	
	public Boolean isValidIDL(IDLSpec idlSpecs, APISpec apiSpecs) {
		return null;
	}
	
	public Request getRandomRequest(IDLSpec idlSpecs, APISpec apiSpecs) {
		return null;
	}
}
