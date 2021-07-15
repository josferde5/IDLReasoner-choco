package idlreasonerchoco.analyzer;

import java.util.List;
import java.util.Map;

import idlreasonerchoco.configuration.IDLConfiguration;
import idlreasonerchoco.configuration.IDLException;

public abstract class Analyzer {

    protected final IDLConfiguration configuration;

    public Analyzer(String specificationType, String apiSpecification, String operationPath, String operationType) throws IDLException {
        this(specificationType, apiSpecification, operationPath, operationType, false);
    }

    public Analyzer(String specificationType, String apiSpecification, String operationPath, String operationType, boolean specAsString) throws IDLException {
        this.configuration = new IDLConfiguration(specificationType, apiSpecification, operationPath, operationType, specAsString);
    }

    public abstract boolean isConsistent() throws IDLException;

    public abstract boolean isDeadParameter(String paramName) throws IDLException;

    public abstract boolean isFalseOptional(String paramName) throws IDLException;

    public abstract Boolean isValidIDL() throws IDLException;
    
    public abstract Map<String, String> getRandomValidRequest() throws IDLException;

    public abstract Map<String, String> getRandomInvalidRequest() throws IDLException;

    public abstract boolean isValidRequest(Map<String, String> request) throws IDLException;

    public abstract boolean isValidPartialRequest(Map<String, String> request) throws IDLException;
    
    public abstract void updateData(Map<String, List<String>> data) throws IDLException;

}