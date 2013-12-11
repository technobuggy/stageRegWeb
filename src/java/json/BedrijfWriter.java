package json;

import domain.Bedrijf;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class BedrijfWriter implements MessageBodyWriter<Bedrijf>
{
    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return Bedrijf.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(Bedrijf message, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return -1;
    }

    @Override
    public void writeTo(Bedrijf bedrijf, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException
    {
        JsonObjectBuilder jsonBedrijf = Json.createObjectBuilder();
        
        jsonBedrijf.add("id", bedrijf.getId());
            if (bedrijf.getNaam() != null)
                jsonBedrijf.add("naam", bedrijf.getNaam());
            if (bedrijf.getAdres() != null )
                jsonBedrijf.add("adres", bedrijf.getAdres());
            if (bedrijf.getGemeente() != null)
                jsonBedrijf.add("gemeente", bedrijf.getGemeente());
            if (bedrijf.getLand() != null)
                jsonBedrijf.add("land", bedrijf.getLand());
            if (bedrijf.getOwner() != null)
                jsonBedrijf.add("owner", bedrijf.getOwner().getUsername());
        
        try (JsonWriter writer = Json.createWriter(entityStream)) {
            writer.writeObject(jsonBedrijf.build());
        }
    } 
}
