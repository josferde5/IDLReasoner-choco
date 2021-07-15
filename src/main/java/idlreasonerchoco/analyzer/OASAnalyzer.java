package idlreasonerchoco.analyzer;

import java.util.List;
import java.util.Map;

import idlreasonerchoco.analyzer.operations.oas.AnalysisOperation;
import idlreasonerchoco.analyzer.operations.oas.OASConsistent;
import idlreasonerchoco.analyzer.operations.oas.OASDeadParameter;
import idlreasonerchoco.analyzer.operations.oas.OASFalseOptional;
import idlreasonerchoco.analyzer.operations.oas.OASRandomRequest;
import idlreasonerchoco.analyzer.operations.oas.OASValidIDL;
import idlreasonerchoco.analyzer.operations.oas.OASValidRequest;
import idlreasonerchoco.analyzer.operations.oas.RequestGenerationOperation;
import idlreasonerchoco.configuration.IDLException;
import idlreasonerchoco.mapper.OASMapper;

public class OASAnalyzer extends Analyzer {

    private OASMapper mapper;

    public OASAnalyzer(String specificationType, String apiSpecification, String operationPath, String operationType) throws IDLException {
        this(specificationType, apiSpecification, operationPath, operationType, false);
    }

    public OASAnalyzer(String specificationType, String apiSpecification, String operationPath, String operationType, boolean specAsString) throws IDLException {
        super(specificationType, apiSpecification, operationPath, operationType, specAsString);
        this.mapper = new OASMapper(configuration, null);
    }

    public boolean isConsistent() throws IDLException {
    	AnalysisOperation consistent = new OASConsistent(mapper);
        return consistent.analyze();
    }

    public boolean isDeadParameter(String paramName) throws IDLException {
        AnalysisOperation deadParameter = new OASDeadParameter(mapper, paramName);
        return deadParameter.analyze();
    }

    public boolean isFalseOptional(String paramName) throws IDLException {
    	AnalysisOperation falseOptional = new OASFalseOptional(mapper, paramName);
        return falseOptional.analyze();
    }

    public Boolean isValidIDL() throws IDLException {
    	AnalysisOperation validIDL = new OASValidIDL(mapper);
        return validIDL.analyze();
    }

    public Map<String, String> getRandomValidRequest() throws IDLException {
    	RequestGenerationOperation randomValidRequest = new OASRandomRequest(mapper, true);
        return randomValidRequest.generate();
    }

    public Map<String, String> getRandomInvalidRequest() throws IDLException {
    	RequestGenerationOperation randomInvalidRequest = new OASRandomRequest(mapper, false);
        return randomInvalidRequest.generate();
    }

    public boolean isValidRequest(Map<String, String> request) throws IDLException {
    	AnalysisOperation validRequest = new OASValidRequest(mapper, request, false);
        return validRequest.analyze();
    }

    public boolean isValidPartialRequest(Map<String, String> request) throws IDLException {
    	AnalysisOperation validPartialRequest = new OASValidRequest(mapper, request, true);
        return validPartialRequest.analyze();
    }
    
    public void updateData(Map<String, List<String>> data) throws IDLException {
    	this.mapper = new OASMapper(configuration, data);
    }

}