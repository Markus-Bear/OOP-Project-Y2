package exception;

/**
 * This exception is thrown when user authentication fails.
 */
public class AuthenticationException extends Exception {

    /**
     * Constructs a new AuthenticationException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the authentication failure.
     */
    public AuthenticationException(String message) {
        super(message);
    }
}
