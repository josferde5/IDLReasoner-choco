package idlreasonerchoco.analyzer.operations;

import idlreasonerchoco.configuration.model.IDLException;

public interface AnalysisOperation {

    boolean analyze() throws IDLException;
}
