/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services;

import domain.StagePlaats;
import domain.user.Login;
import java.net.URI;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author tom
 */

@Path("stageplaatsen")
@Stateless
public class StageplaatsService {
    
     @PersistenceContext(unitName= "stageRegWebPU" )
    private EntityManager em;
    @Context
    private UriInfo uriInfo;
    
    @Resource 
    private Validator validator;
    
    @Context
    private SecurityContext context;
    
    @GET
    @Path("{id}")
    @Produces("application/json")
    public StagePlaats getById(@PathParam("id") int id)  {
        //return (Student) Queries.findResultById(id, Student.class);
        StagePlaats stagePlaats = em.find(StagePlaats.class, id);
        return stagePlaats;
    }
    
    @GET
    @Produces("application/json")
    public List<StagePlaats> getAllStagePlaatsen(){
        //List<Student> studenten = Queries.findAllResults(Student.class);
        Query query = em.createNamedQuery("StagePlaats.findAll");
        List<StagePlaats> stagePlaats = query.getResultList();
        return stagePlaats;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addStagePlaats(StagePlaats stageplaats){
        stageplaats.setId(0);
        
        if (stageplaats.getOwner() == null) {
            stageplaats.setOwner(em.find(Login.class, context.getUserPrincipal().getName()));
        }
     
        
         //Alleen admins mogen berichten voor andere gebruikers toevoegen.
        if (!stageplaats.getOwner().getUsername().equals(context.getUserPrincipal().getName()) && ! context.isUserInRole("admin")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        Set<ConstraintViolation<StagePlaats>> violations = validator.validate(stageplaats);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<StagePlaats> violation : violations) {
                if (errorMessage.length() != 0) {
                    errorMessage.append(",");
                }
                errorMessage.append(violation.getMessage());
            }
            
            errorMessage.append(".");
                
                
            return Response.status(Response.Status.BAD_REQUEST).entity("Kan StagePlaats niet opslaan:" + errorMessage.toString()).build();
        }
        stageplaats.setId((Integer)em.createQuery("select max(s.id) from StagePlaats s").getSingleResult()+1);
        em.persist(stageplaats);
        URI bedrijfUri = uriInfo.getAbsolutePathBuilder().path(" " + stageplaats.getId()).build();
        return Response.created(bedrijfUri).build();
    }
    
    @PUT
    @Path("id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateStagePlaats(@PathParam("id") int id, StagePlaats stagePlaatsUpdate)
    {
        StagePlaats stagePlaats = em.find(StagePlaats.class, id);

        if (stagePlaats == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        em.detach(stagePlaats);
        
        if (stagePlaatsUpdate.getOmschrijving()!= null) {
            stagePlaats.setOmschrijving(stagePlaatsUpdate.getOmschrijving());
        }

        if (stagePlaatsUpdate.getBegeleider()!= null) {
            stagePlaats.setBegeleider(stagePlaatsUpdate.getBegeleider());
        }
        
        if(stagePlaatsUpdate.getGoedgekeurd() == true || stagePlaatsUpdate.getGoedgekeurd() == false)
            stagePlaats.setGoedgekeurd(stagePlaatsUpdate.getGoedgekeurd());
        
        if (stagePlaatsUpdate.getOwner() != null && !stagePlaatsUpdate.getOwner().equals(stagePlaats.getOwner())) {
            
            // Alleen admins mogen de eigenaar van een bericht aanpassen.
            if (context.isUserInRole("admin")) {
                stagePlaats.setOwner(stagePlaatsUpdate.getOwner()); 
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        
        
        
//        if (stagePlaatsUpdate.getTelefoon()!= null) {
//            stagePlaats.setTelefoon(stagePlaatsUpdate.getTelefoon());
//        }
        
        
        Set<ConstraintViolation<StagePlaats>> violations = validator.validate(stagePlaats);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<StagePlaats> violation : violations) {
                if (errorMessage.length() != 0) {
                    errorMessage.append(",");
                }
                errorMessage.append(violation.getMessage());
            }
            
            errorMessage.append(".");
                
                
            return Response.status(Response.Status.BAD_REQUEST).entity("Kan Stageplaats niet opslaan:" + errorMessage.toString()).build();
        }
        em.merge(stagePlaats);
        URI bedrijfUri = uriInfo.getAbsolutePathBuilder().path(" " + stagePlaats.getId()).build();
        return Response.noContent().build(); 
    }
    
    @DELETE
    @Path("id/{id}")
    public Response deleteStagePlaats(@PathParam("id") int id)
    {
        StagePlaats stagePlaats = getById(id);
        if (stagePlaats == null )
            throw new NotFoundException();
        
        if (!stagePlaats.getOwner().getUsername().equals(context.getUserPrincipal().getName()) && ! context.isUserInRole("admin")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        em.remove(stagePlaats);
        return Response.noContent().build();
        //Queries.remove(bedrijf);
    }
    
}
