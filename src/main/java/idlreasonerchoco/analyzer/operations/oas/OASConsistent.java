package idlreasonerchoco.analyzer.operations.oas;

import idlreasonerchoco.mapper.OASMapper;

public class OASConsistent implements AnalysisOperation {

    private final OASMapper mapper;

    public OASConsistent(OASMapper mapper) {
        this.mapper = mapper;
    }
    public boolean analyze() {
        return mapper.getChocoModel().getSolver().solve();
    }
}
