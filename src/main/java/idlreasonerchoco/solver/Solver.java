package idlreasonerchoco.solver;

import org.chocosolver.solver.Model;

import idlreasonerchoco.configuration.IDLException;

/**
 * An Abstract class that initializes choco model object. 
 */
public abstract class Solver {
	
	/**
	 * Choco model object.
	 */
    protected Model chocoModel;

    /**
     * Creates a Solver object and initialize choco model.
     * 
     * @param operationPath operation path.
     * @throws IDLException IDL exception.
     */
	protected Solver(String operationPath) throws IDLException {
        this.chocoModel = new Model(operationPath);
    }

	/**
	 * Returns choco model.
	 * 
	 * @return choco model.
	 */
	public Model getChocoModel() {
        return chocoModel;
    }
}