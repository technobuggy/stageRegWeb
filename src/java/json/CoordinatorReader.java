/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package json;

import domain.user.Login;
import domain.user.Coordinator;
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
public class CoordinatorReader implements MessageBodyReader<Coordinator>
{
    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return Coordinator.class.isAssignableFrom(type);
    }

    @Override
    public Coordinator readFrom(Class<Coordinator> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException
    {
        JsonReader reader = Json.createReader(entityStream);
        JsonObject jsonCoordinator = reader.readObject();
        
        Coordinator coordinator = new Coordinator();
        
        // Getallen in JSON vallen allemaal het type 'number'. 
        JsonNumber jsonId = jsonCoordinator.getJsonNumber("id");
        if (jsonId != null) {
            coordinator.setID(jsonId.intValue());
        } 
        
        // Het tweede argument van de methode getString is de default waarde.
        // Deze wordt gebruikt wanneer het attribuut 'text' niet bestaat.
        coordinator.setFamNaam(jsonCoordinator.getString("famnaam", null));
        coordinator.setVoorNaam(jsonCoordinator.getString("voornaam",null));
        coordinator.setTelefoon(jsonCoordinator.getString("telefoon",null));
        coordinator.setEmail(jsonCoordinator.getString("email",null));
        
        // Van de eigenaar is enkel de username doorgestuurd.
        // We moeten het User-object dus nog opzoeken.
        // We maken hiervoor handmatig een EntityManager aan.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("stageRegWebPU");
        EntityManager em = emf.createEntityManager();
        String username = jsonCoordinator.getString("owner", null);
        if (username != null) {
            Login owner = em.find(Login.class, username);
            coordinator.setOwner(owner);
        }
        em.close();
        emf.close();
        
        
        
        return coordinator;
    }   
}
