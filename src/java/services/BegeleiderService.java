package services;

import javax.ws.rs.Path;
import domain.user.Begeleider;
import domain.user.Login;
import helper.*;
import exceptions.*;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

/**Begeleider operations
 *
 * @version 1.1
 * @author pieter
 */
@Path("begeleiders")
@Stateless
public class BegeleiderService {

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
    public Begeleider getById(@PathParam("id") int id)  {
        //return (Student) Queries.findResultById(id, Student.class);
        Begeleider begeleider = em.find(Begeleider.class, id);
        return begeleider;
    }
    
    @GET
    @Produces("application/json")
    public List<Begeleider> getAllBegeleiders(){
        //List<Student> studenten = Queries.findAllResults(Student.class);
        Query query = em.createNamedQuery("Begeleider.findAll");
        List<Begeleider> begeleiders = query.getResultList();
        return begeleiders;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addStudent(Begeleider begeleider){
        //Queries.add(student);
        begeleider.setID(0);
        
        if (begeleider.getOwner() == null) {
            begeleider.setOwner(em.find(Login.class, context.getUserPrincipal().getName()));
        }
     
        
         //Alleen admins mogen berichten voor andere gebruikers toevoegen.
        if (!begeleider.getOwner().getUsername().equals(context.getUserPrincipal().getName()) && ! context.isUserInRole("admin")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        Set<ConstraintViolation<Begeleider>> violations = validator.validate(begeleider);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<Begeleider> violation : violations) {
                if (errorMessage.length() != 0) {
                    errorMessage.append(",");
                }
                errorMessage.append(violation.getMessage());
            }
            
            errorMessage.append(".");
                
                
            return Response.status(Response.Status.BAD_REQUEST).entity("Kan Begeleider niet opslaan:" + errorMessage.toString()).build();
        }
        begeleider.setID((Integer)em.createQuery("select max(b.ID) from Begeleider b").getSingleResult()+1);
        
        em.persist(begeleider);
        URI bedrijfUri = uriInfo.getAbsolutePathBuilder().path(" " + begeleider.getID()).build();
        return Response.created(bedrijfUri).build();
    }
    
    @PUT
    @Path("id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBegeleider(@PathParam("id") int id, Begeleider begeleiderUpdate)
    {
        Begeleider begeleider = em.find(Begeleider.class, id);

        if (begeleider == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        em.detach(begeleider);
        
        if (begeleiderUpdate.getFamNaam() != null) {
            begeleider.setFamNaam(begeleiderUpdate.getFamNaam());
        }

        if (begeleiderUpdate.getVoorNaam()!= null) {
            begeleider.setVoorNaam(begeleiderUpdate.getVoorNaam());
        }
        
        if (begeleiderUpdate.getOwner() != null && !begeleiderUpdate.getOwner().equals(begeleider.getOwner())) {
            
            // Alleen admins mogen de eigenaar van een bericht aanpassen.
            if (context.isUserInRole("admin")) {
                begeleider.setOwner(begeleiderUpdate.getOwner()); 
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        
        
        
        if (begeleiderUpdate.getEmail()!= null) {
            begeleider.setEmail(begeleiderUpdate.getEmail());
        }
        
        if (begeleiderUpdate.getTelefoon()!= null) {
            begeleider.setTelefoon(begeleiderUpdate.getTelefoon());
        }
        
        
        Set<ConstraintViolation<Begeleider>> violations = validator.validate(begeleider);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<Begeleider> violation : violations) {
                if (errorMessage.length() != 0) {
                    errorMessage.append(",");
                }
                errorMessage.append(violation.getMessage());
            }
            
            errorMessage.append(".");
                
                
            return Response.status(Response.Status.BAD_REQUEST).entity("Kan Begeleider niet opslaan:" + errorMessage.toString()).build();
        }
        em.merge(begeleider);
        URI bedrijfUri = uriInfo.getAbsolutePathBuilder().path(" " + begeleider.getID()).build();
        return Response.noContent().build(); 
    }
    
    
    
    
    @DELETE
    @Path("id/{id}")
    public Response deleteBegeleider(@PathParam("id") int id)
    {
        Begeleider begeleider = getById(id);
        if (begeleider == null )
            throw new NotFoundException();
        
        if (!begeleider.getOwner().getUsername().equals(context.getUserPrincipal().getName()) && ! context.isUserInRole("admin")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        em.remove(begeleider);
        return Response.noContent().build();
        //Queries.remove(bedrijf);
    }
}
