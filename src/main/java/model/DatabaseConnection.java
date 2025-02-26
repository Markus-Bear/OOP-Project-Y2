package model;

import java.sql.*;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/project_db";
    private static final String DB_USERNAME = "markus";
    private static final String DB_PASSWORD = "markusdb";

    // Get a database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    // Close resources (Connection, Statement, ResultSet)
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

    // Close resources (Connection, Statement)
    public static void closeResources(Connection conn, Statement stmt) {
        closeResources(conn, stmt, null);
    }

    // Centralized error logging
    private static void logError(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
    }
}
