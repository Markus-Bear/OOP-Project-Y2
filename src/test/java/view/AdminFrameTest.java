package view;

import model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for the AdminFrame GUI component.
 *
 * <p>
 * This test verifies that an AdminFrame can be created successfully
 * when supplied with a valid user. In a more comprehensive suite,
 * UI tests would use integration tools, but this basic test is intended
 * to catch issues in frame instantiation.
 * </p>
 */
public class AdminFrameTest {

    @Test
    public void testAdminFrameInstantiation() {
        // Create a dummy Admin user for testing.
        User dummyAdmin = new User("A001", "admin@setu.ie", "Admin User", "hashedPassword", "Admin", "", "", null);
        AdminFrame adminFrame = new AdminFrame(dummyAdmin);
        assertNotNull(adminFrame, "AdminFrame should be instantiated successfully.");
    }
}
