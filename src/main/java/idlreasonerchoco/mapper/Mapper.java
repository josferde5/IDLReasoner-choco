package idlreasonerchoco.mapper;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;

import es.us.isa.idl.IDLStandaloneSetupGenerated;
import es.us.isa.idl.generator.IDLGenerator;
import idlreasonerchoco.configuration.IDLConfiguration;
import idlreasonerchoco.configuration.model.ErrorType;
import idlreasonerchoco.configuration.model.Files;
import idlreasonerchoco.configuration.model.IDLException;
import idlreasonerchoco.model.OperationType;
import idlreasonerchoco.utils.ExceptionManager;
import idlreasonerchoco.utils.FileManager;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;

public class Mapper {

	private static final Logger LOG = Logger.getLogger(Mapper.class);

	private static final String OAS_SPECIFICATION_TYPE = "oas";
	private static final String X_DEPENDENCIES = "x-dependencies";

	private final IDLConfiguration configuration;

	public Mapper(IDLConfiguration configuration) throws IDLException {
		this.configuration = configuration;

		if (!this.configuration.getSpecificationType().toLowerCase().equals(OAS_SPECIFICATION_TYPE)) {
			ExceptionManager.rethrow(LOG, ErrorType.BAD_SPECIFICATION.toString());
		}

		if (this.configuration.getIdlPath() == null) {
			this.generateIDLFromOAS();
		}
		
		this.generateConstraintsFromIDL();
		this.mapVariables();
	}

	//TODO mapVariables y cambiar los archivos por objetos
	private void mapVariables() {
	}

	@SuppressWarnings("unchecked")
	public void generateIDLFromOAS() throws IDLException {
        ParseOptions options = new ParseOptions();
        options.setResolveFully(true);
        OpenAPI oasSpec = new OpenAPIV3Parser().read(this.configuration.getApiSpecificationPath());
        Operation oasOp = getOasOperation(oasSpec, this.configuration.getOperationPath(), this.configuration.getOperationType());

        try {
        	List<String> IDLdeps = (List<String>) oasOp.getExtensions().get(X_DEPENDENCIES);
            
            if (!IDLdeps.isEmpty()) {
                String allDeps = String.join(FileManager.NEW_LINE, IDLdeps);
                FileManager.appendContentToFile(this.configuration.getPaths().IDL_AUX_FOLDER + Files.IDL_AUX_FILE, allDeps);
            }
            
        } catch (Exception e) {
        	ExceptionManager.rethrow(LOG, ErrorType.ERROR_READING_DEPENDECIES.toString(), e);
        }
	}

	public void generateConstraintsFromIDL() throws IDLException {
		IDLGenerator idlGenerator = new IDLGenerator();
		idlGenerator.setFolderPath(this.configuration.getPaths().IDL_AUX_FOLDER);
		
		Injector injector = new IDLStandaloneSetupGenerated().createInjectorAndDoEMFRegistration();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		
		String path = this.configuration.getIdlPath() == null
				? this.configuration.getPaths().IDL_AUX_FOLDER + Files.IDL_AUX_FILE
				: this.configuration.getPaths().IDL_AUX_FOLDER + this.configuration.getIdlPath();
		Resource resource = resourceSet.getResource(URI.createFileURI(path), true);
		
		try {
			idlGenerator.doGenerate(resource, null, null);
		} catch (Exception e) {
			ExceptionManager.rethrow(LOG, ErrorType.ERROR_MAPPING_CONSTRAINTS_FROM_IDL.toString(), e);
		}
	}

	private Operation getOasOperation(OpenAPI openAPISpec, String operationPath, String operationType) {
		PathItem item = openAPISpec.getPaths().get(operationPath);

		switch (OperationType.valueOf(operationType)) {
		case GET:
			return item.getGet();
		case DELETE:
			return item.getDelete();
		case HEAD:
			return item.getHead();
		case OPTIONS:
			return item.getOptions();
		case PATCH:
			return item.getPatch();
		case POST:
			return item.getPost();
		case PUT:
			return item.getPut();
		default:
			ExceptionManager.log(LOG, ErrorType.BAD_OAS_OPERATION.toString());
			return null;
		}
	}
}
