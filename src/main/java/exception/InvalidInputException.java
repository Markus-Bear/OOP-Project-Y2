package exception;

/**
 * Exception thrown when an input validation error occurs.
 */
public class InvalidInputException extends Exception {

    /**
     * Constructs a new InvalidInputException with the specified detail message.
     *
     * @param message the detail message explaining the input error.
     */
    public InvalidInputException(String message) {
        super(message);
    }
}
