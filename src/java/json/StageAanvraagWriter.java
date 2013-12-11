/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package json;

import domain.StageAanvraag;
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
public class StageAanvraagWriter implements MessageBodyWriter<StageAanvraag>
{
    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return StageAanvraag.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(StageAanvraag stagePlaats, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return -1;
    }

    @Override
    public void writeTo(StageAanvraag stageAanvraag, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException
    {
        JsonObjectBuilder jsonStageAanvraag = Json.createObjectBuilder();
        
        jsonStageAanvraag.add("id", stageAanvraag.getId());
        
        if (stageAanvraag.getAanvaard()== false || stageAanvraag.getAanvaard() == true)
            jsonStageAanvraag.add("aanvaard", stageAanvraag.getAanvaard());
        
        
        // Van de eigenaar zullen we enkel de username (primary key) doorsturen.
        if (stageAanvraag.getOwner() != null)
            jsonStageAanvraag.add("owner", stageAanvraag.getOwner().getUsername());
        
        if (stageAanvraag.getStudent() != null)
            jsonStageAanvraag.add("studentid", stageAanvraag.getStudent().getID());
        
        if (stageAanvraag.getStageplaats() != null)
            jsonStageAanvraag.add("stageplaatsid", stageAanvraag.getStageplaats().getId());
        
        try (JsonWriter writer = Json.createWriter(entityStream)) {
            writer.writeObject(jsonStageAanvraag.build());
        }
    } 
}
