package controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the PasswordUtils class.
 *
 * <p>
 * These tests ensure that passwords are correctly hashed and that the plain text
 * password can be successfully verified against the generated hash.
 * </p>
 */
public class PasswordUtilsTest {

    @Test
    public void testHashAndVerifyPassword() {
        String plainPassword = "MySecretPass123!";
        String hashedPassword = PasswordUtils.hashPassword(plainPassword);

        assertNotNull(hashedPassword, "Hashed password should not be null.");
        assertFalse(hashedPassword.isEmpty(), "Hashed password should not be empty.");
        assertTrue(PasswordUtils.verifyPassword(plainPassword, hashedPassword),
                "The plain password should match the hashed password.");
    }

    @Test
    public void testVerifyPasswordFails() {
        String plainPassword = "Password1";
        String wrongPassword = "Password2";
        String hashedPassword = PasswordUtils.hashPassword(plainPassword);

        assertFalse(PasswordUtils.verifyPassword(wrongPassword, hashedPassword),
                "Verification should fail for an incorrect password.");
    }
}
