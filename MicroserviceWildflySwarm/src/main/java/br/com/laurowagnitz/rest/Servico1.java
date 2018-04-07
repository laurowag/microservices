package br.com.laurowagnitz.rest;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.laurowagnitz.model.Person;

@Path("")
@RequestScoped
@Transactional
public class Servico1 {

	@Resource
	ManagedExecutorService mes;
	
	@PersistenceContext
	EntityManager em;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listar() {
		return Response.ok().entity(em.createQuery("from Person", Person.class).getResultList()).build();		
	}

	@GET
	@Path("{idToFind}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarId(@PathParam("idToFind") int id) {
		return Response.ok().entity(em.find(Person.class, id)).build();		
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response incluir(@QueryParam("id") int id, @QueryParam("name") String name) {		
		Person person = em.find(Person.class, id);

		if (person == null) {
			person = new Person();
			person.setId(id);
		} 		
		person.setName(name);
		em.persist(person);
		return Response.ok().entity(person).build();		
	}
	
	@GET
	@Path("async")
	public void servicoAsync(@Suspended final AsyncResponse asyncResponse) {
		String initialThread = Thread.currentThread().getName();
		System.out.println("Thread: "+ initialThread + " in action...");
		System.out.println(mes);
		mes.execute(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println(mes);
					String processingThread = Thread.currentThread().getName();

					Thread.sleep(2000);
					String respBody = "Process initated in " + initialThread + " and finished in " + processingThread;
					asyncResponse.resume(Response.ok(respBody).build());                    

				} catch (InterruptedException ex) {
					Logger.getLogger(Servico1.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});

		asyncResponse.setTimeout(3, TimeUnit.SECONDS);
		asyncResponse.setTimeoutHandler(new TimeoutHandler() {
			@Override
			public void handleTimeout(AsyncResponse asyncResponse) {
				asyncResponse.resume(Response.accepted(UUID.randomUUID().toString()).build()); //sending HTTP 202 (Accepted)
			}
		});

		System.out.println(initialThread + " freed ...");
	}

}
