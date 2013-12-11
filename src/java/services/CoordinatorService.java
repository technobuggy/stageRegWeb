package services;

import javax.ws.rs.Path;
import exceptions.*;
import domain.user.Coordinator;
import domain.user.Login;
import helper.*;
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

/**coordinator operations
 *
 * @version 1.1
 * @author pieter
 */
@Path("coordinators")
@Stateless
public class CoordinatorService {

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
    public Coordinator getById(@PathParam("id") int id)  {
        //return (Student) Queries.findResultById(id, Student.class);
        Coordinator coordinator = em.find(Coordinator.class, id);
        return coordinator;
    }
    
    @GET
    @Produces("application/json")
    public List<Coordinator> getAllBegeleiders(){
        //List<Student> studenten = Queries.findAllResults(Student.class);
        Query query = em.createNamedQuery("Coordinator.findAll");
        List<Coordinator> coordinators = query.getResultList();
        return coordinators;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addBegeleider(Coordinator coordinator){
        //Queries.add(student);
        coordinator.setID(0);
        
        if (coordinator.getOwner() == null) {
            coordinator.setOwner(em.find(Login.class, context.getUserPrincipal().getName()));
        }
     
        
         //Alleen admins mogen berichten voor andere gebruikers toevoegen.
        if (!coordinator.getOwner().getUsername().equals(context.getUserPrincipal().getName()) && ! context.isUserInRole("admin")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        Set<ConstraintViolation<Coordinator>> violations = validator.validate(coordinator);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<Coordinator> violation : violations) {
                if (errorMessage.length() != 0) {
                    errorMessage.append(",");
                }
                errorMessage.append(violation.getMessage());
            }
            
            errorMessage.append(".");
                
                
            return Response.status(Response.Status.BAD_REQUEST).entity("Kan Coordinator niet opslaan:" + errorMessage.toString()).build();
        }
        coordinator.setID((Integer)em.createQuery("select max(c.ID) from Coordinator c").getSingleResult()+1);
        em.persist(coordinator);
        URI bedrijfUri = uriInfo.getAbsolutePathBuilder().path(" " + coordinator.getID()).build();
        return Response.created(bedrijfUri).build();
    }
    
    @PUT
    @Path("id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCoordinator(@PathParam("id") int id, Coordinator coordinatorUpdate)
    {
        Coordinator coordinator = em.find(Coordinator.class, id);

        if (coordinator == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        em.detach(coordinator);
        
        if (coordinatorUpdate.getFamNaam() != null) {
            coordinator.setFamNaam(coordinatorUpdate.getFamNaam());
        }

        if (coordinatorUpdate.getVoorNaam()!= null) {
            coordinator.setVoorNaam(coordinatorUpdate.getVoorNaam());
        }
        
        if (coordinatorUpdate.getOwner() != null && !coordinatorUpdate.getOwner().equals(coordinator.getOwner())) {
            
            // Alleen admins mogen de eigenaar van een bericht aanpassen.
            if (context.isUserInRole("admin")) {
                coordinator.setOwner(coordinatorUpdate.getOwner()); 
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
                
        if (coordinatorUpdate.getEmail()!= null) {
            coordinator.setEmail(coordinatorUpdate.getEmail());
        }
        
        if (coordinatorUpdate.getTelefoon()!= null) {
            coordinator.setTelefoon(coordinatorUpdate.getTelefoon());
        }
        
        
        Set<ConstraintViolation<Coordinator>> violations = validator.validate(coordinator);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<Coordinator> violation : violations) {
                if (errorMessage.length() != 0) {
                    errorMessage.append(",");
                }
                errorMessage.append(violation.getMessage());
            }
            
            errorMessage.append(".");
                
                
            return Response.status(Response.Status.BAD_REQUEST).entity("Kan coordinator niet opslaan:" + errorMessage.toString()).build();
        }
        em.merge(coordinator);
        URI bedrijfUri = uriInfo.getAbsolutePathBuilder().path(" " + coordinator.getID()).build();
        return Response.noContent().build(); 
    }
    
    
    
    
    @DELETE
    @Path("id/{id}")
    public Response deleteCoordinator(@PathParam("id") int id)
    {
        Coordinator coordinator = getById(id);
        if (coordinator == null )
            throw new NotFoundException();
        
        if (!coordinator.getOwner().getUsername().equals(context.getUserPrincipal().getName()) && ! context.isUserInRole("admin")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        em.remove(coordinator);
        return Response.noContent().build();
        //Queries.remove(bedrijf);
    }
}
