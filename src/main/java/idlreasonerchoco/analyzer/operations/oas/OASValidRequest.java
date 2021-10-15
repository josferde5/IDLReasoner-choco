package idlreasonerchoco.analyzer.operations.oas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import idlreasonerchoco.configuration.ErrorType;
import idlreasonerchoco.configuration.IDLException;
import idlreasonerchoco.mapper.OASMapper;
import idlreasonerchoco.model.ParameterType;
import idlreasonerchoco.utils.ExceptionManager;
import idlreasonerchoco.utils.Utils;
import io.swagger.v3.oas.models.parameters.Parameter;

/**
 * This class implements AnalysisOperation. It uses {@link #analyze()} to check
 * if the request is valid or not. validity.
 */
public class OASValidRequest implements AnalysisOperation {

	private static final Logger LOG = Logger.getLogger(OASValidRequest.class);

	/**
	 * OASMapper object.
	 * @see OASMapper
	 */
	private final OASMapper mapper;

	/**
	 * request map. It represents parameter name and its value.
	 */
	private final Map<String, String> request;

	/**
	 * partial indicates if the request is partial or not.
	 */
	private final boolean partial;

	/**
	 * Creates OASValidRequest object.
	 * 
	 * @param mapper OASMapper object.
	 * @param request the request to be checked if it is valid or not.
	 * @param partial indicates if the request is partial or not.
	 * @see OASMapper
	 */
	public OASValidRequest(OASMapper mapper, Map<String, String> request, boolean partial) {
		this.mapper = mapper;
		this.request = request;
		this.partial = partial;
	}

	/**
	 * This method analyzes a request/ partial request and show if it is valid or
	 * not. It returns true if the request/partial request is valid. A request is
	 * valid if it satisfies all the dependencies of the IDL specification. A
	 * request is partially valid means that some other parameters should still be
	 * included to make it a full valid request.
	 * 
	 * @return true if the request/partial request is valid, otherwise false.
	 * @throws IDLException IDL exception.
	 */
	public boolean analyze() throws IDLException {
		try {
			if (request != null && request.keySet().stream()
					.allMatch(param -> mapper.getVariablesMap().containsKey(Utils.parseIDLParamName(param)))) {
				List<Constraint> cons = new ArrayList<>();
				mapper.getChocoModel().getSolver().reset();

				for (Parameter parameter : mapper.getParameters()) {
					BoolVar varSet = mapper.getVariablesMap().get(Utils.parseIDLParamName(parameter.getName()) + "Set")
							.asBoolVar();
					if (request.containsKey(parameter.getName())) {
						IntVar paramVar = mapper.getVariablesMap().get(Utils.parseIDLParamName(parameter.getName()))
								.asIntVar();
						Constraint con = mapper.getChocoModel().and(mapper.getChocoModel().arithm(varSet, "=", 1),
								mapper.getChocoModel().arithm(paramVar, "=", mapValueToConstraint(
										request.get(parameter.getName()), parameter.getSchema().getType())));

						cons.add(con);
						con.post();

					} else if (!partial) {
						Constraint setCon = mapper.getChocoModel().arithm(varSet, "=", 0);
						cons.add(setCon);
						setCon.post();
					}
				}

				boolean result = mapper.getChocoModel().getSolver().solve();
				cons.forEach(x -> mapper.getChocoModel().unpost(x));
				mapper.getChocoModel().getSolver().reset();
				return result;

			} else {
				throw new IDLException(ErrorType.ERROR_UNKNOWN_PARAM_IN_REQUEST.toString());
			}

		} catch (IDLException e) {
			ExceptionManager.log(LOG, ErrorType.ERROR_VALIDATING_REQUEST.toString(), e);
			return false;
		}
	}

	private Integer mapValueToConstraint(String paramValue, String type) throws IDLException {
		try {
			switch (ParameterType.valueOf(type.toUpperCase())) {
			case STRING:
			case ARRAY:
				return mapper.getSolver().stringToInt(paramValue);
			case NUMBER:
			case INTEGER:
				return Integer.valueOf(paramValue);
			case BOOLEAN:
				if (Boolean.toString(true).equals(paramValue)) {
					return 1;
				} else if (Boolean.toString(false).equals(paramValue)) {
					return 0;
				}
			default:
				throw new IDLException(
						ErrorType.ERROR_IN_PARAMETER_TYPE.toString() + " -> type: " + type + ", value: " + paramValue);
			}

		} catch (Exception e) {
			ExceptionManager.rethrow(LOG, ErrorType.ERROR_MAPPING_TO_CONSTRAINT.toString(), e);
			return null;
		}
	}
}
