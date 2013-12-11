package exceptions;

/**
 * Exception when the query did not contain :id
 *
 * @author pieter
 */
public class QueryParameterMissmatch extends IllegalArgumentException {

    public QueryParameterMissmatch() {
        super("The query parameters did not match");
    }

    public QueryParameterMissmatch(String message) {
        super(message);
    }

    public QueryParameterMissmatch(String message, Throwable cause) {
        super(message, cause);
    }

    public QueryParameterMissmatch(Throwable cause) {
        super("Id was not found in the database", cause);
    }
}
