package idlreasoner.controller;

import idlreasoner.model.APISpec;
import idlreasoner.model.IDLResponse;
import idlreasoner.model.IDLSpec;
import idlreasoner.model.Parameter;
import idlreasoner.model.Request;

public class IDLReasonerController {

	public IDLResponse checkIsValidRequest(IDLSpec idlSpecs, APISpec apiSpecs, Request request) {
		return null;
	}
	
	public IDLResponse checkIsConsistent(IDLSpec idlSpecs, APISpec apiSpecs) {
		return null;
	}
	
	public IDLResponse checkIsDeadParameter(IDLSpec idlSpecs, APISpec apiSpecs, Parameter request) {
		return null;
	}
	
	public IDLResponse checkIsFalseOptional(IDLSpec idlSpecs, APISpec apiSpecs, Parameter request) {
		return null;
	}
	
	public IDLResponse checkIsValidIDL(IDLSpec idlSpecs, APISpec apiSpecs) {
		return null;
	}
	
	public Request generateRandomRequest(IDLSpec idlSpecs, APISpec apiSpecs) {
		return null;
	}
	
}
