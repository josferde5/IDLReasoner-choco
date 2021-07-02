package idlreasonerchoco.resolutor;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import idlreasonerchoco.configuration.IDLConfiguration;
import idlreasonerchoco.mapper.Mapper;

public class Resolutor {

	private final IDLConfiguration configuration;
	private final Mapper mapper;
	private boolean randomSearch;
	
	public Resolutor(IDLConfiguration configuration, Mapper mapper) {
		this(configuration, mapper, false);
	}
	
	public Resolutor(IDLConfiguration configuration, Mapper mapper, boolean randomSearch) {
		this.configuration = configuration;
		this.randomSearch = randomSearch;
		this.mapper = mapper;
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
