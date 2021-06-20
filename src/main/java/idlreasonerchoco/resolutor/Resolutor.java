package idlreasonerchoco.resolutor;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import idlreasonerchoco.configuration.IDLConfiguration;

public class Resolutor {

	private final IDLConfiguration configuration;
	private boolean randomSearch;
	
	public Resolutor(IDLConfiguration configuration) {
		this(configuration, false);
	}
	
	public Resolutor(IDLConfiguration configuration, boolean randomSearch) {
		this.configuration = configuration;
		this.randomSearch = randomSearch;
	}
	
	public boolean isRandomSearch() {
		return randomSearch;
	}

	public void setRandomSearch(boolean randomSearch) {
		this.randomSearch = randomSearch;
	}
	
	public String getSeed() {
		return ThreadLocalRandom.current().nextInt(1, 2146) + Long.toString((new Date().getTime())/1000).substring(4);
	}
}
