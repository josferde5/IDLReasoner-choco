package idlreasonerchoco.analyzer.operations;

import idlreasonerchoco.mapper.Mapper;

public class Consistent implements AnalysisOperation {

    private final Mapper mapper;

    public Consistent(Mapper mapper) {
        this.mapper = mapper;
    }
    public boolean analyze() {
        return mapper.getChocoModel().getSolver().solve();
    }
}
