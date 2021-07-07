package idlreasonerchoco.analyzer.operations;

import idlreasonerchoco.configuration.model.IDLException;
import idlreasonerchoco.mapper.Mapper;
import io.swagger.v3.oas.models.parameters.Parameter;

public class ValidIDL implements AnalysisOperation {

    private final Mapper mapper;

    public ValidIDL(Mapper mapper) {
        this.mapper = mapper;
    }

    public boolean analyze() throws IDLException {
        Consistent consistent = new Consistent(mapper);
        boolean result = consistent.analyze();
        for (Parameter p : mapper.getParameters()) {
            DeadParameter deadParameter = new DeadParameter(mapper, p.getName());
            FalseOptional falseOptional = new FalseOptional(mapper, p.getName());
            result &= !deadParameter.analyze() && !falseOptional.analyze();
            if (!result) {
                break;
            }
        }
        return result;
    }

}
