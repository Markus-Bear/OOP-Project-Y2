package controller;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
    /**
     * Hashes a plain text password using BCrypt.
     *
     * @param plainPassword the plain text password.
     * @return the hashed password.
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    /**
     * Checks whether the plain password matches the stored hash.
     *
     * @param plainPassword the plain text password.
     * @param hashedPassword the stored hashed password.
     * @return true if the passwords match, false otherwise.
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
