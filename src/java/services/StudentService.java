package services;

import domain.user.Login;
import javax.ws.rs.Path;
import domain.user.Student;
import helper.*;
import exceptions.*;
import java.net.URI;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.validation.Validator;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

/**Student operations
 *
 * URL: /students/
 * 
 * @version 1.1
 * @author pieter
 */
@Path("student")
//@Stateless
@Transactional
public class StudentService {

    /**Returns Student in string format based on ID
     * 
     * URL: /students/[id]
     *
     * @param id ID of the Student
     * @return Student in string format
     * @throws IllegalIdException ID wasn't well formed
     * @throws IdNotFoundException ID wasn't found in db
     * @throws QueryParameterMissmatch Student query didn't use :id
     */
    
    @PersistenceContext
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
    public Student getById(@PathParam("id") int id)  {
        //return (Student) Queries.findResultById(id, Student.class);
        Student student = em.find(Student.class, id);
        return student;
    }
    
    @GET
    @Produces("application/json")
    public List<Student> getAllStudents(){
        //List<Student> studenten = Queries.findAllResults(Student.class);
        Query query = em.createNamedQuery("Student.findAll");
        List<Student> studenten = query.getResultList();
        return studenten;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addStudent(Student student){
        student.setID(0);
        
        if (student.getOwner() == null) {
            student.setOwner(em.find(Login.class, context.getUserPrincipal().getName()));
        }
     
        
         //Alleen admins mogen berichten voor andere gebruikers toevoegen.
        if (!student.getOwner().getUsername().equals(context.getUserPrincipal().getName()) && ! context.isUserInRole("admin")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<Student> violation : violations) {
                if (errorMessage.length() != 0) {
                    errorMessage.append(",");
                }
                errorMessage.append(violation.getMessage());
            }
            
            errorMessage.append(".");
                
                
            return Response.status(Response.Status.BAD_REQUEST).entity("Kan Student niet opslaan:" + errorMessage.toString()).build();
        }
        student.setID((Integer)em.createQuery("select max(s.ID) from Student s").getSingleResult()+1);
        em.persist(student);
        URI bedrijfUri = uriInfo.getAbsolutePathBuilder().path(" " + student.getID()).build();
        return Response.created(bedrijfUri).build();
    }
    
    @PUT
    @Path("id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateStudent(@PathParam("id") int id, Student studentUpdate)
    {
        Student student = em.find(Student.class, id);

        if (student == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        em.detach(student);
        
        if (studentUpdate.getFamNaam() != null) {
            student.setFamNaam(studentUpdate.getFamNaam());
        }

        if (studentUpdate.getVoorNaam()!= null) {
            student.setVoorNaam(studentUpdate.getVoorNaam());
        }
        
        if (studentUpdate.getOwner() != null && !studentUpdate.getOwner().equals(student.getOwner())) {
            
            // Alleen admins mogen de eigenaar van een bericht aanpassen.
            if (context.isUserInRole("admin")) {
                student.setOwner(studentUpdate.getOwner()); 
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        
        if (studentUpdate.getAfstudeerRichting()!= null) {
            student.setAfstudeerRichting(studentUpdate.getAfstudeerRichting());
        }
        
        if (studentUpdate.getEmail()!= null) {
            student.setEmail(studentUpdate.getEmail());
        }
        
        if (studentUpdate.getTelefoon()!= null) {
            student.setTelefoon(studentUpdate.getTelefoon());
        }
        
        
        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<Student> violation : violations) {
                if (errorMessage.length() != 0) {
                    errorMessage.append(",");
                }
                errorMessage.append(violation.getMessage());
            }
            
            errorMessage.append(".");
                
                
            return Response.status(Response.Status.BAD_REQUEST).entity("Kan Student niet opslaan:" + errorMessage.toString()).build();
        }
        em.merge(student);
        URI bedrijfUri = uriInfo.getAbsolutePathBuilder().path(" " + student.getID()).build();
        return Response.noContent().build(); 
    }
    
    
    
    
    @DELETE
    @Path("id/{id}")
    public Response deleteStudent(@PathParam("id") int id)
    {
        Student student = getById(id);
        if (student == null )
            throw new NotFoundException();
        
        if (!student.getOwner().getUsername().equals(context.getUserPrincipal().getName()) && ! context.isUserInRole("admin")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        em.remove(student);
        return Response.noContent().build();
        //Queries.remove(bedrijf);
    }
    
}
