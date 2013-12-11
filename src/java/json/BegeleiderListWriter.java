/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package json;

import domain.user.Begeleider;
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

/**
 *
 * @author tom
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class BegeleiderListWriter implements MessageBodyWriter<List<Begeleider>>
{
    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        if (!List.class.isAssignableFrom(type)) {
            return false;
        }

        // Controleer of de generische parameter T van de List<T> de klasse Message was.
        if (genericType instanceof ParameterizedType) {
            Type[] arguments = ((ParameterizedType) genericType).getActualTypeArguments();
            return arguments.length == 1 && arguments[0].equals(Begeleider.class);
        } else {
            return false;
        }
    }

    @Override
    public long getSize(List<Begeleider> messages, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return -1;
    }

    @Override
    public void writeTo(List<Begeleider> studenten, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException
    {
        JsonArrayBuilder jsonBegeleiders = Json.createArrayBuilder();
        
        for (Begeleider begeleider : studenten) {
            JsonObjectBuilder jsonBegeleider = Json.createObjectBuilder();
        
       
        jsonBegeleider.add("id", begeleider.getID());
        if (begeleider.getVoorNaam() != null)
            jsonBegeleider.add("voornaam", begeleider.getVoorNaam());
        if (begeleider.getFamNaam() != null)
            jsonBegeleider.add("famnaam", begeleider.getFamNaam());
        if (begeleider.getEmail() != null)
            jsonBegeleider.add("email", begeleider.getEmail());
        if (begeleider.getTelefoon() != null)
            jsonBegeleider.add("telefoon", begeleider.getTelefoon());
        if (begeleider.getEmail() != null)
            jsonBegeleider.add("email", begeleider.getEmail());
        
        // Van de eigenaar zullen we enkel de username (primary key) doorsturen.
        if (begeleider.getOwner() != null)
            jsonBegeleider.add("owner", begeleider.getOwner().getUsername());
        if (begeleider.getBedrijf() != null)
            jsonBegeleider.add("bedrijfId", begeleider.getBedrijf().getId());
        
        
            
        
            
            jsonBegeleiders.add(jsonBegeleider);
        }
        
        try (JsonWriter writer = Json.createWriter(entityStream)) {
            writer.writeArray(jsonBegeleiders.build());
        }
    }
}
