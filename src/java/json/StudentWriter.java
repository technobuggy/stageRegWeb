package json;

import domain.user.Student;
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
public class StudentWriter implements MessageBodyWriter<Student>
{
    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return Student.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(Student student, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return -1;
    }

    @Override
    public void writeTo(Student student, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException
    {
        JsonObjectBuilder jsonStudent = Json.createObjectBuilder();
        
        jsonStudent.add("id", student.getID());
        if (student.getVoorNaam() != null)
            jsonStudent.add("voornaam", student.getVoorNaam());
        if (student.getFamNaam() != null)
            jsonStudent.add("famnaam", student.getFamNaam());
        if (student.getEmail() != null)
            jsonStudent.add("email", student.getEmail());
        if (student.getTelefoon() != null)
            jsonStudent.add("telefoon", student.getTelefoon());
        if (student.getAfstudeerRichting()!= null)
            jsonStudent.add("afstudeerrichting", student.getAfstudeerRichting());
        if (student.getEmail() != null)
            jsonStudent.add("email", student.getEmail());
        
        // Van de eigenaar zullen we enkel de username (primary key) doorsturen.
        if (student.getOwner() != null)
            jsonStudent.add("owner", student.getOwner().getUsername());
        
        
        try (JsonWriter writer = Json.createWriter(entityStream)) {
            writer.writeObject(jsonStudent.build());
        }
    } 
}
