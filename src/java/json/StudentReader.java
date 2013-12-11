package json;

import domain.user.Student;
import domain.user.Login;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Calendar;
import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class StudentReader implements MessageBodyReader<Student>
{
    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return Student.class.isAssignableFrom(type);
    }

    @Override
    public Student readFrom(Class<Student> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException
    {
        JsonReader reader = Json.createReader(entityStream);
        JsonObject jsonStudent = reader.readObject();
        
        Student student = new Student();
        
        // Getallen in JSON vallen allemaal het type 'number'. 
        JsonNumber jsonId = jsonStudent.getJsonNumber("id");
        if (jsonId != null) {
            student.setID(jsonId.intValue());
        } 
        
        // Het tweede argument van de methode getString is de default waarde.
        // Deze wordt gebruikt wanneer het attribuut 'text' niet bestaat.
        student.setFamNaam(jsonStudent.getString("famnaam", null));
        student.setVoorNaam(jsonStudent.getString("voornaam",null));
        student.setAfstudeerRichting(jsonStudent.getString("telefoonnummer",null));
        student.setTelefoon(jsonStudent.getString("telefoon",null));
        student.setEmail(jsonStudent.getString("email",null));
        
        // Van de eigenaar is enkel de username doorgestuurd.
        // We moeten het User-object dus nog opzoeken.
        // We maken hiervoor handmatig een EntityManager aan.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("stageRegWebPU");
        EntityManager em = emf.createEntityManager();
        String username = jsonStudent.getString("owner", null);
        if (username != null) {
            Login owner = em.find(Login.class, username);
            student.setOwner(owner);
        }
        em.close();
        emf.close();
        
        
        
        return student;
    }
}
