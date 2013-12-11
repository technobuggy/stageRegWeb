/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import exceptions.IdNotFoundException;
import exceptions.QueryParameterMissmatch;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**Implements various, often reused query methods
 *
 * @version 1.1
 * @author pieter
 */
public final class Queries {

    @PersistenceContext(unitName= "stageRegWebPU" )
    private static EntityManager em;
    @Context
    private UriInfo uriInfo;

    private Queries() {

    }

    /**Basic single result by ID method with custom queries.
     *
     * @param id query id
     * @param entityObjectClass the class in which the query resides
     * @param namedQuery the name of the query to be used
     * @return toString() result of the queried object
     * @throws IllegalArgumentException All exceptions regarding ID's
     */
    public static String getResultById(int id, Class entityObjectClass, String namedQuery) throws IllegalArgumentException {
        //validate the input id parameter
        idValidator.validate(id);

        //Build query        
        Query query = em.createNamedQuery(namedQuery, entityObjectClass);
        try {
            query.setParameter("id", id);
        } catch (Exception e) {
            //throw new exception with the original stack trace
            throw new QueryParameterMissmatch("The query id parameter is not set to \":id\" in query " + namedQuery, e);
        }

        //Fetch results
        List results = query.getResultList();

        //Check results
        if (results.isEmpty()) {
            throw new IdNotFoundException();
        } else {
            return results.get(0).toString();
        }
    }

    //TODO: check if query exists
    public static <T> List<T> findAllResults(Class<T> entity) throws QueryParameterMissmatch {
        try{
            Query query = em.createNamedQuery(entity.getName() + ".findAll");
            List<T> lijst = query.getResultList();
            return lijst;
        } catch (Exception e) {
            throw new QueryParameterMissmatch("No findAll parameter found", e);
        }
        
    }
    /**
     * Uses entitymanagers find method to fetch data based on id
     * 
     * @param id The id to query
     * @param entity The entity class to be used
     * @return Returns an object with the results
     */
    public static Object findResultById(String id, Class entity) {
        
        return em.find(entity, id);
        
        
    }

    
   


    /**
     * Persists an object to the database
     * @param entity The object to add
     */

    public static void add(Object entity) {
        em.persist(entity);
    }
    /** Updates an entity
     * 
     * @param entity The object to modify
     */
    public static void update(Object entity) {
        em.merge(entity);
    }
    /**Removes an object
     * 
     * @param entity The object to remove
     */
    public static void remove(Object entity) {
        em.remove(entity);
    }
}
