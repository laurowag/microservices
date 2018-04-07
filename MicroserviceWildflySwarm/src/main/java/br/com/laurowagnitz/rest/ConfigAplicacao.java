package br.com.laurowagnitz.rest;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationScoped
@ApplicationPath("")
public class ConfigAplicacao extends Application {
	
	@PostConstruct
	public void postConstruct() {
		System.out.println("CONSTRUIU");
		/*
		Topology topology;
		try {
			topology = Topology.lookup();
			topology.advertise(System.getProperty("applicationName"), "tag1", "tag2");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/		
	}

}
