package json;

import domain.user.Student;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
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
public class StudentListWriter implements MessageBodyWriter<List<Student>>
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
            return arguments.length == 1 && arguments[0].equals(Student.class);
        } else {
            return false;
        }
    }

    @Override
    public long getSize(List<Student> messages, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return -1;
    }

    @Override
    public void writeTo(List<Student> studenten, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException
    {
        JsonArrayBuilder jsonStudenten = Json.createArrayBuilder();
        
        for (Student student : studenten) {
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
        
        
        
        
        
            
            jsonStudenten.add(jsonStudent);
        }
        
        try (JsonWriter writer = Json.createWriter(entityStream)) {
            writer.writeArray(jsonStudenten.build());
        }
    }
}
