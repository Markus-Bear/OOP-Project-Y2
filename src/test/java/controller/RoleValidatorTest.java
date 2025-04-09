package controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import exception.RoleAccessException;

public class RoleValidatorTest {

    @Test
    public void testValidRole() {
        assertDoesNotThrow(() -> RoleValidator.validateRole("Admin", "Admin", "MediaStaff"));
    }

    @Test
    public void testInvalidRole() {
        Exception exception = assertThrows(RoleAccessException.class, () -> {
            RoleValidator.validateRole("Student", "Admin", "MediaStaff");
        });
        assertTrue(exception.getMessage().contains("Access denied"));
    }

    @Test
    public void testNullRole() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            RoleValidator.validateRole(null, "Admin");
        });
        assertTrue(exception.getMessage().contains("User role cannot be null or empty"));
    }
}
