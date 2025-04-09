package model;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Provides methods for establishing and closing connections to the database.
 * <p>
 * The connection parameters are loaded from a properties file for better security.
 * </p>
 */
public class DatabaseConnection {
    private static final Properties properties = new Properties();

    // Load the properties file in a static block so it runs once when the class is loaded.
    static {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("Database properties file not found in the classpath.");
            }
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load database properties.", e);
        }
    }

    /**
     * Returns a new database connection using the properties loaded from the file.
     *
     * @return a {@link Connection} object representing the database connection.
     * @throws SQLException if a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        String dbUrl = properties.getProperty("db.url");
        String dbUsername = properties.getProperty("db.username");
        String dbPassword = properties.getProperty("db.password");

        return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }

    /**
     * Closes the provided {@link ResultSet}, {@link Statement}, and {@link Connection} resources.
     * Any exceptions thrown while closing these resources are logged.
     *
     * @param conn the {@link Connection} to close (may be null).
     * @param stmt the {@link Statement} to close (may be null).
     * @param rs   the {@link ResultSet} to close (may be null).
     */
    public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            logError("Error closing ResultSet", e);
        }

        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            logError("Error closing Statement", e);
        }

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            logError("Error closing Connection", e);
        }
    }

    /**
     * Closes the provided {@link Statement} and {@link Connection} resources.
     *
     * @param conn the {@link Connection} to close (may be null).
     * @param stmt the {@link Statement} to close (may be null).
     */
    public static void closeResources(Connection conn, Statement stmt) {
        closeResources(conn, stmt, null);
    }

    /**
     * Logs an error message along with the associated {@link SQLException}.
     *
     * @param message the error message to log.
     * @param e       the {@link SQLException} that was caught.
     */
    private static void logError(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
    }
}
