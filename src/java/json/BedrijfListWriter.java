package json;

import domain.Bedrijf;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class BedrijfListWriter implements MessageBodyWriter<List<Bedrijf>>
{
    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        if (!List.class.isAssignableFrom(type)) {
            return false;
        }

        if (genericType instanceof ParameterizedType) {
            Type[] arguments = ((ParameterizedType) genericType).getActualTypeArguments();
            return arguments.length == 1 && arguments[0].equals(Bedrijf.class);
        } else {
            return false;
        }
    }

    @Override
    public long getSize(List<Bedrijf> messages, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return -1;
    }

    @Override
    public void writeTo(List<Bedrijf> bedrijven, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException
    {
        JsonArrayBuilder jsonBedrijven = Json.createArrayBuilder();
        
        for (Bedrijf bedrijf : bedrijven) {
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
            jsonBedrijven.add(jsonBedrijf);
            if (bedrijf.getOwner() != null)
                jsonBedrijf.add("owner", bedrijf.getOwner().getUsername());
        }
        
        try (JsonWriter writer = Json.createWriter(entityStream)) {
            writer.writeArray(jsonBedrijven.build());
        }
    }
}
