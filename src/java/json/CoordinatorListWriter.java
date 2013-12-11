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
public class CoordinatorListWriter implements MessageBodyWriter<List<Coordinator>>
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
            return arguments.length == 1 && arguments[0].equals(Coordinator.class);
        } else {
            return false;
        }
    }

    @Override
    public long getSize(List<Coordinator> messages, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return -1;
    }

    @Override
    public void writeTo(List<Coordinator> coordinators, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException
    {
        JsonArrayBuilder jsonCoordinators = Json.createArrayBuilder();
        
        for (Coordinator coordinator : coordinators) {
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
        
        
        
        
        
            
            jsonCoordinators.add(jsonCoordinator);
        }
        
        try (JsonWriter writer = Json.createWriter(entityStream)) {
            writer.writeArray(jsonCoordinators.build());
        }
    }
}