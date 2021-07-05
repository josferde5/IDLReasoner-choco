package idlreasonerchoco.analyzer;

public class DeadParameter implements OperationAnalysis {

    public DeadParameter(String paramName) {
    }

    public boolean analyze() {
        return false;
    }
}
