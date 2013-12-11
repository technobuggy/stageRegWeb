/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services;

import domain.Bedrijf;
import domain.user.Login;
import domain.Bedrijf;
import helper.Queries;
import java.net.URI;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
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
@Path("bedrijven")
//Stateless
//@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class BedrijfService {
    
    
    @PersistenceContext
    private EntityManager em;
    @Context
    private UriInfo uriInfo;
    
    @Resource 
    private Validator validator;
    
    @Context
    private SecurityContext context;
    
    
    //get localhost:8080/mijnapp/api/products/testnieuw
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Bedrijf findBedrijf(@PathParam("id") int id)
    {
        //return "sletten!";
        Bedrijf bedrijf = em.find(Bedrijf.class, id);
        return bedrijf;
        //return (Bedrijf) Queries.findResultById(id, Bedrijf.class);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Bedrijf> findAlleBedrijven()
    {
        //Query query = em.createQuery("Select b from Bedrijf b");
        Query query = em.createNamedQuery("Bedrijf.findAll");
        List<Bedrijf> bedrijven = query.getResultList();
        return bedrijven;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response toevoegenBedrijf(Bedrijf bedrijf)
    {
        bedrijf.setId(0);
        
        if (bedrijf.getOwner() == null) {
            bedrijf.setOwner(em.find(Login.class, context.getUserPrincipal().getName()));
        }
     
        
        if (!bedrijf.getOwner().getUsername().equals(context.getUserPrincipal().getName()) && ! context.isUserInRole("admin")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        Set<ConstraintViolation<Bedrijf>> violations = validator.validate(bedrijf);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<Bedrijf> violation : violations) {
                if (errorMessage.length() != 0) {
                    errorMessage.append(",");
                }
                errorMessage.append(violation.getMessage());
            }
            
            errorMessage.append(".");
                
                
            return Response.status(Response.Status.BAD_REQUEST).entity("Kan Bedrijf niet opslaan:" + errorMessage.toString()).build();
        }
        bedrijf.setId((Integer)em.createQuery("select max(b.id) from Bedrijf b").getSingleResult()+1);
        em.persist(bedrijf);
        URI bedrijfUri = uriInfo.getAbsolutePathBuilder().path(" " + bedrijf.getId()).build();
        return Response.created(bedrijfUri).build();
    }
    
    @PUT
    @Path("id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBedrijf(@PathParam("id") int id, Bedrijf bedrijfUpdate)
    {
        Bedrijf bedrijf = em.find(Bedrijf.class, id);

        if (bedrijf == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        em.detach(bedrijf);
        
        if (bedrijfUpdate.getNaam()!= null) {
            bedrijf.setNaam(bedrijfUpdate.getNaam());
        }

        if (bedrijfUpdate.getAdres()!= null) {
            bedrijf.setAdres(bedrijfUpdate.getAdres());
        }
        
        if (bedrijfUpdate.getOwner() != null && !bedrijfUpdate.getOwner().equals(bedrijf.getOwner())) {
            
            // Alleen admins mogen de eigenaar van een bericht aanpassen.
            if (context.isUserInRole("admin")) {
                bedrijf.setOwner(bedrijfUpdate.getOwner()); 
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        
        if (bedrijfUpdate.getGemeente()!= null) {
            bedrijf.setGemeente(bedrijfUpdate.getGemeente());
        }
        
        if (bedrijfUpdate.getLand()!= null) {
            bedrijf.setLand(bedrijfUpdate.getLand());
        }
        
        
        
        
        Set<ConstraintViolation<Bedrijf>> violations = validator.validate(bedrijf);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<Bedrijf> violation : violations) {
                if (errorMessage.length() != 0) {
                    errorMessage.append(",");
                }
                errorMessage.append(violation.getMessage());
            }
            
            errorMessage.append(".");
                
                
            return Response.status(Response.Status.BAD_REQUEST).entity("Kan Bedrijf niet opslaan:" + errorMessage.toString()).build();
        }
        em.merge(bedrijf);
        URI bedrijfUri = uriInfo.getAbsolutePathBuilder().path(" " + bedrijf.getId()).build();
        return Response.noContent().build(); 
    }
    
    
    
    @DELETE
    @Path("id/{id}")
    public Response removeBedrijf(@PathParam("id") int id)
    {
        Bedrijf bedrijf = findBedrijf(id);
        if (bedrijf == null )
            throw new NotFoundException();
        
        if (!bedrijf.getOwner().getUsername().equals(context.getUserPrincipal().getName()) && ! context.isUserInRole("admin")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        em.remove(bedrijf);
        return Response.noContent().build();
        //Queries.remove(bedrijf);
    }
    
    public List<Bedrijf> findBedrijvenStad(String stad)
    {
        Query query = em.createQuery("Select b from Bedrijf b where b.gemeente :=stad");
        query.setParameter("stad", stad);
        List<Bedrijf> bedrijven = query.getResultList();
        return bedrijven;
    }
}
