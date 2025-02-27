package exception;

/**
 * This exception is thrown when a user attempts to access a resource or perform an action
 * that is not permitted for their role.
 */
public class RoleAccessException extends Exception {

    /**
     * Constructs a new RoleAccessException with the specified detail message.
     *
     * @param message the detail message explaining the role access violation.
     */
    public RoleAccessException(String message) {
        super(message);
    }
}
