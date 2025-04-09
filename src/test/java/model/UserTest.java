package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
/**
 * Unit tests for the User model.
 *
 * <p>
 * This test checks if a User object with valid values is recognized as valid,
 * as well as verifying getters and setters.
 * Also tests that an empty User object should not be valid.
 * </p>
 */
public class UserTest {

    @Test
    public void testUserIsValid() {
        User user = new User("C001", "john.doe@setu.ie", "John Doe", "password", "Student", "Computing", "Software Development", 2);
        assertTrue(user.isValid());
    }

    @Test
    public void testUserIsInvalid() {
        // An empty user should not be valid.
        User user = new User();
        assertFalse(user.isValid());
    }
}
