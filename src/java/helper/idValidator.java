package helper;
import exceptions.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**Checks id's, every id should be checked the same way
 *
 * @version 1.0
 * @author pieterdb
 */
public class idValidator {
    //singleton variable
    private static final Logger GLASSFISHLOGGER = Logger.getLogger(idValidator.class.getName());
    
    //prevent instantiation
    private idValidator(){
        //enables glassfish logging
        
    };
    /**validates the id
     * 
     * @param id the id to be validated
     * @throws IllegalIdException an exception when the id ain't valid
     */
    public static void validate(int id) throws IllegalIdException{
        if (id <= 0){
            GLASSFISHLOGGER.log(Level.SEVERE, "Illegal ID demanded: {0}", id);
            throw new IllegalIdException();
        }
    }
}
