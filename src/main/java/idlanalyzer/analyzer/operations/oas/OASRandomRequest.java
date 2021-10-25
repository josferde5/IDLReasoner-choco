package idlanalyzer.analyzer.operations.oas;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import idlanalyzer.configuration.ErrorType;
import idlanalyzer.configuration.IDLException;
import idlanalyzer.mapper.OASMapper;
import idlanalyzer.model.ParameterType;
import idlanalyzer.utils.ExceptionManager;
import idlanalyzer.utils.Utils;
import io.swagger.v3.oas.models.parameters.Parameter;

/**
 * This class implements RequestGenerationOperation interface. It uses
 * {@link #generate()} to generate random requests.
 * 
 */
public class OASRandomRequest implements RequestGenerationOperation {

	private static final Logger LOG = Logger.getLogger(OASRandomRequest.class);

	/**
	 * OASMapper object.
	 * @see OASMapper
	 */
	private final OASMapper mapper;

	/**
	 * valid indicates if the request is valid or not valid.
	 */
	private final boolean valid;

	/**
	 * Creates OASRandomRequest object.
	 * 
	 * @param mapper OASMapper object.
	 * @param valid  indicates if the request is valid or not valid
	 * @see OASMapper
	 */
	public OASRandomRequest(OASMapper mapper, boolean valid) {
		this.mapper = mapper;
		this.valid = valid;
	}

	public Map<String, String> generate() throws IDLException {
		mapper.getChocoModel().getSolver().reset();
		Map<String, String> request = null;
		if (valid) {
			request = mapRequest();
			mapper.getChocoModel().getSolver().reset();
		} else {
			mapper.restartSolver(false);
			if (mapper.getChocoModel().getCstrs().length > 0) {
				request = mapRequest();
			}
			mapper.restartSolver(true);
		}
		return request;
	}

	private Map<String, String> mapRequest() throws IDLException {
		Map<String, String> request = null;
		OASConsistent consistent = new OASConsistent(mapper);
		if (consistent.analyze()) {
			request = new HashMap<>();
			for (Parameter parameter : mapper.getParameters()) {
				BoolVar varSet = mapper.getVariablesMap().get(Utils.parseIDLParamName(parameter.getName()) + "Set")
						.asBoolVar();
				if (varSet.getValue() == 1) {
					IntVar paramVar = mapper.getVariablesMap().get(Utils.parseIDLParamName(parameter.getName()))
							.asIntVar();
					request.put(parameter.getName(),
							mapConstraintToValue(paramVar.getValue(), parameter.getSchema().getType()));
				}
			}
		}
		return request;
	}

	private String mapConstraintToValue(Integer intValue, String type) throws IDLException {
		switch (ParameterType.valueOf(type.toUpperCase())) {
		case STRING:
		case ARRAY:
			return mapper.getStringToIntMap().inverse().get(intValue) != null
					? mapper.getStringToIntMap().inverse().get(intValue)
					: "toString" + intValue;
		case NUMBER:
		case INTEGER:
			return intValue.toString();
		case BOOLEAN:
			return Boolean.toString(intValue == 1);
		default:
			ExceptionManager.rethrow(LOG, ErrorType.ERROR_IN_PARAMETER_TYPE.toString() + " :" + type);
			return null;
		}
	}
}
