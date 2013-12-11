/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package json;

import domain.StagePlaats;
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
public class StagePlaatsListWriter implements MessageBodyWriter<List<StagePlaats>>
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
            return arguments.length == 1 && arguments[0].equals(StagePlaats.class);
        } else {
            return false;
        }
    }

    @Override
    public long getSize(List<StagePlaats> messages, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return -1;
    }

    @Override
    public void writeTo(List<StagePlaats> stagePlaatsen, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException
    {
        JsonArrayBuilder jsonStagePlaatsen = Json.createArrayBuilder();
        
        for (StagePlaats stagePlaats : stagePlaatsen) {
            JsonObjectBuilder jsonStagePlaats = Json.createObjectBuilder();
        
       
        jsonStagePlaats.add("id", stagePlaats.getId());
        if (stagePlaats.getOmschrijving() != null)
            jsonStagePlaats.add("omschrijving", stagePlaats.getOmschrijving());
        if (stagePlaats.getAantal()!= 0)
            jsonStagePlaats.add("aantal", stagePlaats.getAantal());
        if (stagePlaats.getGoedgekeurd()== false || stagePlaats.getGoedgekeurd() == true)
            jsonStagePlaats.add("goedgekeurd", stagePlaats.getGoedgekeurd());
        
        
        // Van de eigenaar zullen we enkel de username (primary key) doorsturen.
        if (stagePlaats.getOwner() != null)
            jsonStagePlaats.add("owner", stagePlaats.getOwner().getUsername());
        
        if (stagePlaats.getBegeleider() != null)
            jsonStagePlaats.add("begeleiderid", stagePlaats.getBegeleider().getID());
           
            
            jsonStagePlaatsen.add(jsonStagePlaats);
        }
        
        try (JsonWriter writer = Json.createWriter(entityStream)) {
            writer.writeArray(jsonStagePlaatsen.build());
        }
    }
}
