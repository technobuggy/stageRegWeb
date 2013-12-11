/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services;

import domain.user.Login;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author tom
 */
@Path("credentials")
@Transactional
public class Credentials {
    @PersistenceContext
    private EntityManager em;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkCredentials(InputStream in){
      JsonObject credentials = Json.createReader(in).readObject();  
      
      String username = credentials.getString("username", null);
      
      if (username == null) {
          return Response.status(Status.BAD_REQUEST).entity("Username niet ingevuld").build();
      }
      
      String password = credentials.getString("password", null);
      
      if (password == null){
          return Response.status(Status.BAD_REQUEST).entity("Password niet ingevuld").build();
      }
      
      Login existingUser = em.find(Login.class, username);
      if (existingUser == null) {
          return Response.status(Status.BAD_REQUEST).entity("Ongeldige username").build();
      }
      
      Login inputUser = new Login();
      inputUser.setPassword(password);
      inputUser.encryptPassword();
      
      if(!inputUser.getEcryptedPassword().equals(existingUser.getEcryptedPassword())) {
          return Response.status(Status.BAD_REQUEST).entity("Ongeldig paswoord").build();
      }
      
      JsonArrayBuilder rolesArray = Json.createArrayBuilder();
      for (String role: existingUser.getRoles()) {
          rolesArray.add(role);
      }
      
      return Response.ok(rolesArray.build()).build();
    }
   
}
