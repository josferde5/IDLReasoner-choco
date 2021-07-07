package idlreasonerchoco.analyzer;

import java.util.Map;

import idlreasonerchoco.analyzer.operations.*;

import idlreasonerchoco.configuration.IDLConfiguration;
import idlreasonerchoco.configuration.model.IDLException;
import idlreasonerchoco.mapper.Mapper;

public class Analyzer {

    private final Mapper mapper;
    private final IDLConfiguration configuration;

    public Analyzer(String specificationType, String apiSpecification, String operationPath, String operationType) throws IDLException {
        this(specificationType, apiSpecification, operationPath, operationType, true);
    }

    public Analyzer(String specificationType, String apiSpecification, String operationPath, String operationType, boolean chargeFromFile) throws IDLException {
        this.configuration = new IDLConfiguration(specificationType, apiSpecification, operationPath, operationType, chargeFromFile);
        this.mapper = new Mapper(configuration);
    }

    public boolean isConsistent() {
        Consistent consistent = new Consistent(mapper);
        return consistent.analyze();
    }

    public boolean isDeadParameter(String paramName) throws IDLException {
        DeadParameter deadParameter = new DeadParameter(mapper, paramName);
        return deadParameter.analyze();
    }

    public boolean isFalseOptional(String paramName) throws IDLException {
        FalseOptional falseOptional = new FalseOptional(mapper, paramName);
        return falseOptional.analyze();
    }

    public Boolean isValidIDL() throws IDLException {
        ValidIDL validIDL = new ValidIDL(mapper);
        return validIDL.analyze();
    }

    public Map<String, String> getRandomValidRequest() throws IDLException {
        RandomRequest randomValidRequest = new RandomRequest(mapper, true);
        return randomValidRequest.generate();
    }

    public Map<String, String> getRandomInvalidRequest() throws IDLException {
        RandomRequest randomInvalidRequest = new RandomRequest(mapper, false);
        return randomInvalidRequest.generate();
    }

    public boolean isValidRequest(Map<String, String> request) throws IDLException {
        ValidRequest validRequest = new ValidRequest(mapper, request, false);
        return validRequest.analyze();
    }

    public boolean isValidPartialRequest(Map<String, String> request) throws IDLException {
        ValidRequest validPartialRequest = new ValidRequest(mapper, request, true);
        return validPartialRequest.analyze();
    }

}