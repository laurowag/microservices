package br.com.laurowagnitz;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.laurowagnitz.rest.Servico1;

//@RunWith(Arquillian.class)
//@DefaultDeployment
public class MainTest {
	
	@Inject
	Servico1 servico1;
	
    //@Test
    public void testMyComponent() {    	
    	assertEquals(servico1.consultaVersao().getStatus(), 200);
    }
    
    //@Test
    public void testMyComponent2() {    	
    	Client client = ClientBuilder.newClient();
    	Response response = client.target("http://localhost:8080").path("/version").request(MediaType.APPLICATION_JSON).get();    	
       	assertEquals(response.getStatus(), 200);
    }

}
