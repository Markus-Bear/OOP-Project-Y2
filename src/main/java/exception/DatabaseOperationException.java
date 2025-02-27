package exception;

/**
 * This exception is thrown when a database operation fails.
 */
public class DatabaseOperationException extends Exception {

    /**
     * Constructs a new DatabaseOperationException with the specified detail message and cause.
     *
     * @param message the detail message explaining the error.
     * @param cause   the underlying cause of the error.
     */
    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
