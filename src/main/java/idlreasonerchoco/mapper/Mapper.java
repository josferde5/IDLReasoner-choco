package idlreasonerchoco.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.Variable;

import com.google.common.collect.HashBiMap;

import idlreasonerchoco.configuration.IDLConfiguration;
import idlreasonerchoco.configuration.IDLException;

public abstract class Mapper {
	
    protected final IDLConfiguration configuration;
    protected HashBiMap<String, Integer> stringToIntMap;
    protected Map<String, Variable> variablesMap;
    protected Model chocoModel;

    protected Mapper(IDLConfiguration configuration) throws IDLException {
        this.configuration = configuration;
        this.chocoModel = new Model(configuration.getOperationPath());
        this.variablesMap = new HashMap<>();
        this.stringToIntMap = HashBiMap.create();
        this.stringToIntMap.put("default", 0);
    }

    protected abstract void mapVariables(Map<String, List<String>> data) throws IDLException;

    public Model getChocoModel() {
        return chocoModel;
    }
    
    public HashBiMap<String, Integer> getStringToIntMap() {
        return stringToIntMap;
    }
    
    public Map<String, Variable> getVariablesMap() {
        return variablesMap;
    }
}
