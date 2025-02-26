package exception;

// Custom exception for invalid role access
public class RoleAccessException extends Exception {
    public RoleAccessException(String message) {
        super(message);
    }
}
