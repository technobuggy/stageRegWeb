package json;

import domain.Bedrijf;
import domain.user.Login;
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

@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class BedrijfReader implements MessageBodyReader<Bedrijf>
{
    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return Bedrijf.class.isAssignableFrom(type);
    }

    @Override
    public Bedrijf readFrom(Class<Bedrijf> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException
    {
        JsonReader reader = Json.createReader(entityStream);
        JsonObject jsonBedrijf = reader.readObject();
        
        Bedrijf bedrijf = new Bedrijf();
        
        JsonNumber jsonId = jsonBedrijf.getJsonNumber("id");
        if (jsonId != null) {
            bedrijf.setId(jsonId.intValue());
        } 
                
        bedrijf.setNaam(jsonBedrijf.getString("naam", null));
        bedrijf.setAdres(jsonBedrijf.getString("adres",null));
        bedrijf.setGemeente(jsonBedrijf.getString("gemeente",null));
        bedrijf.setLand(jsonBedrijf.getString("land",null));
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("stageRegWebPU");
        EntityManager em = emf.createEntityManager();
        String username = jsonBedrijf.getString("owner", null);
        if (username != null) {
            Login owner = em.find(Login.class, username);
            bedrijf.setOwner(owner);
        }
        em.close();
        emf.close();
        
        return bedrijf;
    }
}
