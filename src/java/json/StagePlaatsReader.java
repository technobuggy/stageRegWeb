/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package json;

import domain.user.Login;
import domain.StagePlaats;
import domain.user.Begeleider;
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
public class StagePlaatsReader implements MessageBodyReader<StagePlaats>
{
    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return StagePlaats.class.isAssignableFrom(type);
    }

    @Override
    public StagePlaats readFrom(Class<StagePlaats> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException
    {
        JsonReader reader = Json.createReader(entityStream);
        JsonObject jsonStagePlaats = reader.readObject();
        
        StagePlaats stagePlaats = new StagePlaats();
        
        // Getallen in JSON vallen allemaal het type 'number'. 
        JsonNumber jsonId = jsonStagePlaats.getJsonNumber("id");
        if (jsonId != null) {
            stagePlaats.setId(jsonId.intValue());
        } 
        
        // Het tweede argument van de methode getString is de default waarde.
        // Deze wordt gebruikt wanneer het attribuut 'text' niet bestaat.
        stagePlaats.setOmschrijving(jsonStagePlaats.getString("omschrijving", null));
        if (jsonStagePlaats.containsKey("aantal"))
            stagePlaats.setAantal(jsonStagePlaats.getInt("aantal"));
        if (jsonStagePlaats.containsKey("goedgekeurd"))
            stagePlaats.setGoedgekeurd(jsonStagePlaats.getBoolean("goedgekeurd",false));
        
        
        // Van de eigenaar is enkel de username doorgestuurd.
        // We moeten het User-object dus nog opzoeken.
        // We maken hiervoor handmatig een EntityManager aan.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("stageRegWebPU");
        EntityManager em = emf.createEntityManager();
        String username = jsonStagePlaats.getString("owner", null);
        if (username != null) {
            Login owner = em.find(Login.class, username);
            stagePlaats.setOwner(owner);
        }
        JsonNumber begeleiderId = jsonStagePlaats.getJsonNumber("begeleiderid");   
        if (begeleiderId != null){
            Begeleider begeleider = em.find(Begeleider.class, begeleiderId.intValue());
            stagePlaats.setBegeleider(begeleider);
        }
    
        em.close();
        emf.close();
        
        
        
        return stagePlaats;
    }
}
