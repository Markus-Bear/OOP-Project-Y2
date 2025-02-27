package view;

import model.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

/**
 * The Update class provides a utility method to convert all user passwords in the database
 * from plaintext to hashed format using BCrypt.
 * <p>
 * This class was created to hash all the passwords stored as plaintext in the users table.
 * After hashing, users can log in securely using the BCrypt check in the authentication logic.
 * </p>
 */
public class HashExistingPlaintextPasswords {

    /**
     * Hashes all plaintext passwords in the users database table.
     * <p>
     * This method retrieves every user's password from the database and checks if the password
     * is in plaintext. If so, it uses BCrypt to hash the password and updates the corresponding
     * database record with the hashed password.
     * </p>
     * <p>
     * Note: This method skips passwords that are already hashed by checking common BCrypt prefixes.
     * Ensure that you backup your database before executing this update.
     * </p>
     */
    public static void hashAllPasswords() {
        Connection conn = null;
        PreparedStatement selectStmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            // Retrieve all user records with their current passwords
            String selectSQL = "SELECT user_id, password FROM users";
            selectStmt = conn.prepareStatement(selectSQL);
            rs = selectStmt.executeQuery();

            // Prepare update statement
            String updateSQL = "UPDATE users SET password = ? WHERE user_id = ?";
            updateStmt = conn.prepareStatement(updateSQL);

            while (rs.next()) {
                String userId = rs.getString("user_id");
                String currentPassword = rs.getString("password");

                // Skip if the password is already hashed (common BCrypt prefixes)
                if (currentPassword.startsWith("$2a$") || currentPassword.startsWith("$2b$") || currentPassword.startsWith("$2y$")) {
                    continue;
                }

                // Hash the plain-text password
                String hashedPassword = BCrypt.hashpw(currentPassword, BCrypt.gensalt());

                // Update the database record with the hashed password
                updateStmt.setString(1, hashedPassword);
                updateStmt.setString(2, userId);
                updateStmt.executeUpdate();
            }

            System.out.println("All plain-text passwords have been successfully hashed.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Ensure all resources are closed properly
            DatabaseConnection.closeResources(conn, selectStmt, rs);
            DatabaseConnection.closeResources(conn, updateStmt);
        }
    }

    /**
     * The main method to run the password hashing utility.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        hashAllPasswords();
    }
}
