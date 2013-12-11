package exceptions;

/**
 * Exception when the queried ID was not found in the database
 *
 * @author pieter
 */
public class IdNotFoundException extends IllegalArgumentException {

    public IdNotFoundException() {
        super("Id was not found in the database");
    }

    public IdNotFoundException(String message) {
        super(message);
    }

    public IdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdNotFoundException(Throwable cause) {
        super("Id was not found in the database", cause);
    }
}
