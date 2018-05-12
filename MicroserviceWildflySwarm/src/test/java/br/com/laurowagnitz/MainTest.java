package br.com.laurowagnitz;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.ws.rs.client.Client;	
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

import br.com.laurowagnitz.rest.Servico1;

@RunWith(Arquillian.class)
public class MainTest {
	
	@Inject
	Servico1 servico1;
	
	@Deployment
	public static Archive<JAXRSArchive> createDeployment() {
	    JAXRSArchive deployment = ShrinkWrap.create( JAXRSArchive.class );
	    try {
	    	deployment.addPackage("br.com.laurowagnitz.model");
	    	deployment.addResource(Servico1.class);	    	
	    } catch (Exception erro) {
	    	erro.printStackTrace();
	    }
	    return deployment;
	}		

    @Test
    public void testMyComponent() {    	
    	assertEquals(servico1.consultaVersao().getStatus(), 200);
    }
    
    @Test
    public void testMyComponent2() {    	
    	Client client = ClientBuilder.newClient();
    	Response response = client.target("http://localhost:8080").path("/version").request(MediaType.APPLICATION_JSON).get();    	
       	assertEquals(response.getStatus(), 200);
    }

}
