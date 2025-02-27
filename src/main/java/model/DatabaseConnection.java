package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Provides methods for establishing and closing connections to the database.
 * <p>
 * The connection parameters are configured as constants.
 * </p>
 */
public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/project_db";
    private static final String DB_USERNAME = "markus";
    private static final String DB_PASSWORD = "markusdb";

    /**
     * Returns a new database connection using the configured URL, username, and password.
     *
     * @return a {@link Connection} object representing the database connection.
     * @throws SQLException if a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
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
