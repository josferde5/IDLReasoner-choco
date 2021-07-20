package idlreasonerchoco.solver;

import org.chocosolver.solver.Model;

import idlreasonerchoco.configuration.IDLException;

public abstract class Solver {
    protected Model chocoModel;

	protected Solver(String operationPath) throws IDLException {
        this.chocoModel = new Model(operationPath);
    }

	public Model getChocoModel() {
        return chocoModel;
    }
}