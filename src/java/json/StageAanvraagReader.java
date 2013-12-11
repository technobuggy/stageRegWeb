/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package json;

import domain.StageAanvraag;
import domain.StagePlaats;
import domain.user.Login;
import domain.user.Student;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
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

/**
 *
 * @author tom
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class StageAanvraagReader implements MessageBodyReader<StageAanvraag>
{
    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return StageAanvraag.class.isAssignableFrom(type);
    }

    @Override
    public StageAanvraag readFrom(Class<StageAanvraag> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException
    {
        JsonReader reader = Json.createReader(entityStream);
        JsonObject jsonStageAanvraag = reader.readObject();
        
        StageAanvraag stageAanvraag = new StageAanvraag();
        
        // Getallen in JSON vallen allemaal het type 'number'. 
        JsonNumber jsonId = jsonStageAanvraag.getJsonNumber("id");
        if (jsonId != null) {
            stageAanvraag.setId(jsonId.intValue());
        } 
        
        // Het tweede argument van de methode getString is de default waarde.
        // Deze wordt gebruikt wanneer het attribuut 'text' niet bestaat.
        if (jsonStageAanvraag.containsKey("aanvaard"))
            stageAanvraag.setAanvaard(jsonStageAanvraag.getBoolean("aanvaard",false));
        
        
        // Van de eigenaar is enkel de username doorgestuurd.
        // We moeten het User-object dus nog opzoeken.
        // We maken hiervoor handmatig een EntityManager aan.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("stageRegWebPU");
        EntityManager em = emf.createEntityManager();
        String username = jsonStageAanvraag.getString("owner", null);
        if (username != null) {
            Login owner = em.find(Login.class, username);
            stageAanvraag.setOwner(owner);
        }
        JsonNumber studentId = jsonStageAanvraag.getJsonNumber("studentid");   
        if (studentId != null){
            Student student = em.find(Student.class, studentId.intValue());
            stageAanvraag.setStudent(student);
        }
        JsonNumber stagePlaatsId = jsonStageAanvraag.getJsonNumber("stageplaatsid");   
        if (stagePlaatsId != null){
            StagePlaats stagePlaats = em.find(StagePlaats.class, stagePlaatsId.intValue());
            stageAanvraag.setStageplaats(stagePlaats);
        }
        
    
        em.close();
        emf.close();
        
        
        
        return stageAanvraag;
    }
}
