package br.com.laurowagnitz.rest;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.laurowagnitz.model.Cliente;
import br.com.laurowagnitz.model.Receita;

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
	public Response servicoSync() {
		
		Cliente cli = em.find(Cliente.class, 2);
		
		Receita rec = em.find(Receita.class, 21);
		
		if (rec == null) {
			rec = new Receita();
		}
		
		rec.setId(21);
		rec.setCliente(cli);
		rec.setRt(cli);
		rec.setData(new Date());
		rec.setRecomendacao("Eee deu certo!!!");
		em.persist(rec);
		
		//A View permite eu alterar a outra classe
		cli.setNome(cli.getNome() + ".");
		em.persist(cli);
		
		em.refresh(cli);
		
		for (String key : System.getenv().keySet()) {
			System.out.println("****"+key+": "+System.getenv(key));
		}
		
		TypedQuery<Receita> sql = em.createQuery("select rec from Receita rec join fetch rec.cliente join fetch rec.rt", Receita.class);
		return Response.ok().entity(sql.getResultList()).build();		
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
