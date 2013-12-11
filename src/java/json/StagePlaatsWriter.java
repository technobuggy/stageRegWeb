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

/**
 *
 * @author tom
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class StagePlaatsWriter implements MessageBodyWriter<StagePlaats>
{
    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return StagePlaats.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(StagePlaats stagePlaats, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return -1;
    }

    @Override
    public void writeTo(StagePlaats stagePlaats, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException
    {
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
        try (JsonWriter writer = Json.createWriter(entityStream)) {
            writer.writeObject(jsonStagePlaats.build());
        }
    } 
}
