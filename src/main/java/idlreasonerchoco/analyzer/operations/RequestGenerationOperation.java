package idlreasonerchoco.analyzer.operations;

import idlreasonerchoco.configuration.model.IDLException;

import java.util.Map;

public interface RequestGenerationOperation {

    Map<String, String> generate() throws IDLException;
}
