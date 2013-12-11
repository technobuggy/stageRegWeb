/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package json;

import domain.user.Coordinator;
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
public class CoordinatorWriter implements MessageBodyWriter<Coordinator>
{
    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return Coordinator.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(Coordinator student, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return -1;
    }

    @Override
    public void writeTo(Coordinator coordinator, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException
    {
        JsonObjectBuilder jsonCoordinator = Json.createObjectBuilder();
        
        jsonCoordinator.add("id", coordinator.getID());
        if (coordinator.getVoorNaam() != null)
            jsonCoordinator.add("voornaam", coordinator.getVoorNaam());
        if (coordinator.getFamNaam() != null)
            jsonCoordinator.add("famnaam", coordinator.getFamNaam());
        if (coordinator.getEmail() != null)
            jsonCoordinator.add("email", coordinator.getEmail());
        if (coordinator.getTelefoon() != null)
            jsonCoordinator.add("telefoon", coordinator.getTelefoon());
        if (coordinator.getEmail() != null)
            jsonCoordinator.add("email", coordinator.getEmail());
        
        // Van de eigenaar zullen we enkel de username (primary key) doorsturen.
        if (coordinator.getOwner() != null)
            jsonCoordinator.add("owner", coordinator.getOwner().getUsername());
        
        
        try (JsonWriter writer = Json.createWriter(entityStream)) {
            writer.writeObject(jsonCoordinator.build());
        }
    } 
}