package exception;

// Custom exception for database operation failures
public class DatabaseOperationException extends Exception {
    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
