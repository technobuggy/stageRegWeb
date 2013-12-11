package exceptions;

/**
 * Exception when the id was not well formed
 *
 * @author pieter
 */
public class IllegalIdException extends IllegalArgumentException {

    public IllegalIdException() {
        super("Illegal ID");
    }

    public IllegalIdException(String message) {
        super(message);
    }

    public IllegalIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalIdException(Throwable cause) {
        super("Illegal ID", cause);
    }
}
