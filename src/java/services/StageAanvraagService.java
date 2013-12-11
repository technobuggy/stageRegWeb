/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services;

import domain.StageAanvraag;
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

@Path("stageaanvragen")
@Stateless
public class StageAanvraagService {
    
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
    public StageAanvraag getById(@PathParam("id") int id)  {
        //return (Student) Queries.findResultById(id, Student.class);
        StageAanvraag stageAanvraag = em.find(StageAanvraag.class, id);
        return stageAanvraag;
    }
    
    @GET
    @Produces("application/json")
    public List<StageAanvraag> getAllStageAanvragen(){
        //List<Student> studenten = Queries.findAllResults(Student.class);
        Query query = em.createNamedQuery("StageAanvraag.findAll");
        List<StageAanvraag> stageAanvraag = query.getResultList();
        return stageAanvraag;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addStageAanvraag(StageAanvraag stageAanvraag){
        stageAanvraag.setId(0);
        
        if (stageAanvraag.getOwner() == null) {
            stageAanvraag.setOwner(em.find(Login.class, context.getUserPrincipal().getName()));
        }
     
        
         
        if (!stageAanvraag.getOwner().getUsername().equals(context.getUserPrincipal().getName()) && ! context.isUserInRole("admin")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        Set<ConstraintViolation<StageAanvraag>> violations = validator.validate(stageAanvraag);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<StageAanvraag> violation : violations) {
                if (errorMessage.length() != 0) {
                    errorMessage.append(",");
                }
                errorMessage.append(violation.getMessage());
            }
            
            errorMessage.append(".");
                
                
            return Response.status(Response.Status.BAD_REQUEST).entity("Kan Stageaanvraag niet opslaan:" + errorMessage.toString()).build();
        }
        stageAanvraag.setId((Integer)em.createQuery("select max(s.id) from StageAanvraag s").getSingleResult()+1);
        em.persist(stageAanvraag);
        URI bedrijfUri = uriInfo.getAbsolutePathBuilder().path(" " + stageAanvraag.getId()).build();
        return Response.created(bedrijfUri).build();
    }
    
    @PUT
    @Path("id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateStageAanvraag(@PathParam("id") int id, StageAanvraag stageAanvraagUpdate)
    {
        StageAanvraag stageAanvraag = em.find(StageAanvraag.class, id);

        if (stageAanvraag == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        em.detach(stageAanvraag);
        
        if (stageAanvraagUpdate.getAanvaard()== false || stageAanvraagUpdate.getAanvaard() == true) {
            stageAanvraag.setAanvaard(stageAanvraagUpdate.getAanvaard());
        }

        if (stageAanvraagUpdate.getStudent()!= null) {
            stageAanvraag.setStudent(stageAanvraagUpdate.getStudent());
        }
        
        if (stageAanvraagUpdate.getStageplaats() != null)
            stageAanvraag.setStageplaats(stageAanvraagUpdate.getStageplaats());
        
        
        if (stageAanvraagUpdate.getOwner() != null && !stageAanvraagUpdate.getOwner().equals(stageAanvraag.getOwner())) {
            
            // Alleen admins mogen de eigenaar van een bericht aanpassen.
            if (context.isUserInRole("admin")) {
                stageAanvraag.setOwner(stageAanvraagUpdate.getOwner()); 
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        
        
        
//        if (stagePlaatsUpdate.getTelefoon()!= null) {
//            stagePlaats.setTelefoon(stagePlaatsUpdate.getTelefoon());
//        }
        
        
        Set<ConstraintViolation<StageAanvraag>> violations = validator.validate(stageAanvraag);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<StageAanvraag> violation : violations) {
                if (errorMessage.length() != 0) {
                    errorMessage.append(",");
                }
                errorMessage.append(violation.getMessage());
            }
            
            errorMessage.append(".");
                
                
            return Response.status(Response.Status.BAD_REQUEST).entity("Kan Stageplaats niet opslaan:" + errorMessage.toString()).build();
        }
        em.merge(stageAanvraag);
        URI bedrijfUri = uriInfo.getAbsolutePathBuilder().path(" " + stageAanvraag.getId()).build();
        return Response.noContent().build(); 
    }
    
    @DELETE
    @Path("id/{id}")
    public Response deleteStageAanvraag(@PathParam("id") int id)
    {
        StageAanvraag stageAanvraag = getById(id);
        if (stageAanvraag == null )
            throw new NotFoundException();
        
         if (!stageAanvraag.getOwner().getUsername().equals(context.getUserPrincipal().getName()) && ! context.isUserInRole("admin")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        em.remove(stageAanvraag);
        return Response.noContent().build();
        //Queries.remove(bedrijf);
    }
    
}
