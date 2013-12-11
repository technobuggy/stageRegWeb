/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package json;

import domain.Bedrijf;
import domain.user.Login;
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
public class BegeleiderReader implements MessageBodyReader<Begeleider>
{
    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return Begeleider.class.isAssignableFrom(type);
    }

    @Override
    public Begeleider readFrom(Class<Begeleider> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException
    {
        JsonReader reader = Json.createReader(entityStream);
        JsonObject jsonBegeleider = reader.readObject();
        
        Begeleider begeleider = new Begeleider();
        
        // Getallen in JSON vallen allemaal het type 'number'. 
        JsonNumber jsonId = jsonBegeleider.getJsonNumber("id");
        if (jsonId != null) {
            begeleider.setID(jsonId.intValue());
        } 
        
        // Het tweede argument van de methode getString is de default waarde.
        // Deze wordt gebruikt wanneer het attribuut 'text' niet bestaat.
        begeleider.setFamNaam(jsonBegeleider.getString("famnaam", null));
        begeleider.setVoorNaam(jsonBegeleider.getString("voornaam",null));
        begeleider.setTelefoon(jsonBegeleider.getString("telefoon",null));
        begeleider.setEmail(jsonBegeleider.getString("email",null));
        
        // Van de eigenaar is enkel de username doorgestuurd.
        // We moeten het User-object dus nog opzoeken.
        // We maken hiervoor handmatig een EntityManager aan.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("stageRegWebPU");
        EntityManager em = emf.createEntityManager();
        String username = jsonBegeleider.getString("owner", null);
        if (username != null) {
            Login owner = em.find(Login.class, username);
            begeleider.setOwner(owner);
        }
        JsonNumber bedrijfId = jsonBegeleider.getJsonNumber("bedrijfid");   
        if (bedrijfId != null){
            Bedrijf bedrijf = em.find(Bedrijf.class, bedrijfId);
            begeleider.setBedrijf(bedrijf);
        }
    
        em.close();
        emf.close();
        
        
        
        return begeleider;
    }  
}
