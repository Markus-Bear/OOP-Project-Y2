package controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PasswordUtilsTest {

    @Test
    public void testHashAndVerifyPassword() {
        String plainPassword = "mySecret123";
        String hash = PasswordUtils.hashPassword(plainPassword);
        assertNotNull(hash);
        // The plain password should verify against the generated hash
        assertTrue(PasswordUtils.verifyPassword(plainPassword, hash));
    }
}
