package idlreasonerchoco.analyzer;

import org.apache.log4j.Logger;

import idlreasonerchoco.configuration.ErrorType;
import idlreasonerchoco.configuration.IDLConfiguration;
import idlreasonerchoco.configuration.IDLException;
import idlreasonerchoco.mapper.Mapper;
import idlreasonerchoco.model.APISpec;
import idlreasonerchoco.model.IDLSpec;
import idlreasonerchoco.model.Parameter;
import idlreasonerchoco.model.Request;
import idlreasonerchoco.resolutor.Resolutor;

public class Analyzer {

	private final static Logger LOG = Logger.getLogger(Analyzer.class);
	
	private final Mapper mapper;
	private final Resolutor resolutor;
	private final IDLConfiguration configuration;
	
	private boolean lastRandomReqWasValid;
	private boolean needReloadConstraintsFile;
	
	public Analyzer() throws IDLException {
		this.configuration = new IDLConfiguration();
		this.needReloadConstraintsFile = true;
		this.lastRandomReqWasValid = false;
		this.resolutor = null;
		this.mapper = null;
	}
	/*
	 * Una instancia del Analyzer sirve para solucionar las dependencias de un método,
	 * en nuestro caso el specificationType siempre va a ser (de momento) OAS por lo que podemos prescindir del parámetro (DE MOMENTO),
	 * en el IDLPath irá el archivo con las restricciones ** (Mismas consideraciones que para el APISpecPath)
	 * en ApiSpecPath ira el path de la especificación de la API, habría que repensar de que forma pasarla, ya que actualmente al usar solo
	 * la librería en RESTest sirve pasar el PATH del archivo, pero si queremos exponerlo como servicio necesitamos otro constructor
	 * que reciba la especificación de alguna otra forma ya que no tendremos la especificación en local,
	 * los parámetros relativos a la operación que va a analizarse de momento no tienen ninguna consideración.
	 * Clase de interés en IDLReasoner -> MapperCreator (Ahora mismo en RESTest se carga una vez la especificación del archivo y 
	 * luego otra en la librería, por lo que atendiendo a lo que decía en APISpecPath, deberíamos pasar la especificación de otra 
	 * forma para evitar este tipo de redundacia de operaciones)
	 */
	public Analyzer(String specificationType, String idlPath, String apiSpecificationPath, String operationPath, String operationType) throws IDLException {
		this.configuration = new IDLConfiguration();
		this.needReloadConstraintsFile = true;
		this.lastRandomReqWasValid = false;
		this.resolutor = null;
		this.mapper = null;
	}
	
	public Boolean isValidRequest(IDLSpec idlSpecs, APISpec apiSpecs, Request request) {
		return null;
	}
	
	public Boolean isConsistent(IDLSpec idlSpecs, APISpec apiSpecs) {
		return null;
	}
	
	public Boolean isDeadParameter(IDLSpec idlSpecs, APISpec apiSpecs, Parameter request) {
		return null;
	}
	
	public Boolean isFalseOptional(IDLSpec idlSpecs, APISpec apiSpecs, Parameter request) {
		return null;
	}
	
	public Boolean isValidIDL(IDLSpec idlSpecs, APISpec apiSpecs) {
		return null;
	}
	
	public Request getRandomRequest(IDLSpec idlSpecs, APISpec apiSpecs) {
		return null;
	}
	
	
	public static void main(String... args) throws IDLException {
		new Analyzer();
	}
}
