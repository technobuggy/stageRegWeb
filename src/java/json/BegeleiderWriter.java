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
import java.lang.reflect.Type;
import java.math.BigDecimal;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.ws.rs.Produces;
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
@Produces(MediaType.APPLICATION_JSON)
public class BegeleiderWriter implements MessageBodyWriter<Begeleider>
{
    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return Begeleider.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(Begeleider begeleider, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return -1;
    }

    @Override
    public void writeTo(Begeleider begeleider, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException
    {
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
        
        try (JsonWriter writer = Json.createWriter(entityStream)) {
            writer.writeObject(jsonBegeleider.build());
        }
    } 
}