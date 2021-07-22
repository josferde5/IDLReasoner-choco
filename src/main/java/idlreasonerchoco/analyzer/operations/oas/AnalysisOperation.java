package idlreasonerchoco.analyzer.operations.oas;

import idlreasonerchoco.configuration.IDLException;

public interface AnalysisOperation {

    boolean analyze() throws IDLException;
}
