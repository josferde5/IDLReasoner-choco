package idlreasonerchoco.configuration;

import java.util.Date;

public class Paths {
	
	public final String ROOT;
	public final String RESOURCES_PATH;
	public final String IDL_AUX_FOLDER;

	public Paths() {
		ROOT = "/";
		RESOURCES_PATH = "src/main/resources/";
		IDL_AUX_FOLDER = "idl_aux_files/" + Long.toString(new Date().getTime()) + "/";
	}
}
