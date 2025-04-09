package exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for custom exception classes.
 *
 * <p>
 * This test suite instantiates each custom exception and confirms that the
 * error message is correctly stored and retrievable.
 * </p>
 */
public class ExceptionTest {

    @Test
    public void testAuthenticationException() {
        String message = "Authentication failed";
        AuthenticationException ex = new AuthenticationException(message);
        assertEquals(message, ex.getMessage());
    }

    @Test
    public void testDatabaseOperationException() {
        String message = "Database error occurred";
        Exception cause = new Exception("Underlying cause");
        DatabaseOperationException ex = new DatabaseOperationException(message, cause);
        assertEquals(message, ex.getMessage());
    }

    @Test
    public void testInvalidInputException() {
        String message = "Invalid input provided";
        InvalidInputException ex = new InvalidInputException(message);
        assertEquals(message, ex.getMessage());
    }

    @Test
    public void testReservationConflictException() {
        String message = "Reservation conflict encountered";
        ReservationConflictException ex = new ReservationConflictException(message);
        assertEquals(message, ex.getMessage());
    }

    @Test
    public void testRoleAccessException() {
        String message = "Access denied due to role restrictions";
        RoleAccessException ex = new RoleAccessException(message);
        assertEquals(message, ex.getMessage());
    }
}
