package idlreasonerchoco.analyzer.operations.oas;

import java.util.Map;

import idlreasonerchoco.configuration.IDLException;

public interface RequestGenerationOperation {

    Map<String, String> generate() throws IDLException;
}
