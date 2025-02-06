package model;

import model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    //Fetch all Users
    public List<User> getAllUsers() {
        String query = "SELECT u.user_id, u.email, u.role, u.name, " +
                "s.course, s.department AS student_department, s.year, " +
                "l.department AS lecturer_department " +
                "FROM users u " +
                "LEFT JOIN students s ON u.user_id = s.student_id " +
                "LEFT JOIN lecturers l ON u.user_id = l.lecturer_id";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getString("user_id"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setName(rs.getString("name"));

                if ("Student".equals(user.getRole())) {
                    user.setCourse(rs.getString("course"));
                    user.setDepartment(rs.getString("student_department"));
                    user.setYear(rs.getInt("year"));
                } else if ("Lecturer".equals(user.getRole())) {
                    user.setDepartment(rs.getString("lecturer_department"));
                }

                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all users: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }

        return users;
    }

    public User getUserById(String userId) {
        // Updated query to fetch both student and lecturer data
        String query = "SELECT u.*, " +
                "s.course, s.department AS student_department, s.year, " +
                "l.department AS lecturer_department " +
                "FROM users u " +
                "LEFT JOIN students s ON u.user_id = s.student_id " +
                "LEFT JOIN lecturers l ON u.user_id = l.lecturer_id " +
                "WHERE u.user_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, userId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getString("user_id"));
                user.setEmail(rs.getString("email"));
                user.setName(rs.getString("name"));
                user.setRole(rs.getString("role"));

                // Populate role-specific fields
                if ("Student".equals(user.getRole())) {
                    user.setCourse(rs.getString("course"));
                    user.setDepartment(rs.getString("student_department"));
                    user.setYear(rs.getInt("year"));
                } else if ("Lecturer".equals(user.getRole())) {
                    user.setDepartment(rs.getString("lecturer_department"));
                }

                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
        return null;
    }

    public String getUserRole(String userId) {
        String query = "SELECT role FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            } else {
                return null;
            }

        } catch (SQLException e) {
            System.err.println("Error getting role: " + e.getMessage());
            return null;
        }
    }

    public boolean addUser(User user, String creatorId) {
        String sql = "{CALL AddUser(?, ?, ?, ?, ?, ?, ?, ?)}";
        Connection conn = null;
        CallableStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareCall(sql);

            // Set parameters with explicit null handling
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getName());

            // Handle course field
            if (user.getCourse() != null) {
                stmt.setString(5, user.getCourse());
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }

            // Handle department field
            if (user.getDepartment() != null) {
                stmt.setString(6, user.getDepartment());
            } else {
                stmt.setNull(6, Types.VARCHAR);
            }

            // Handle year field
            if (user.getYear() != null) {
                stmt.setInt(7, user.getYear());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }

            stmt.setString(8, creatorId);

            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return false;
        } finally {
            try {
                DatabaseConnection.closeResources(conn, stmt, null);
            } catch (Exception e) {
                System.err.println("Resource cleanup error: " + e.getMessage());
            }
        }
    }

    public boolean updateUser(User user, String adminId) {
        String sql = "{CALL UpdateUser(?, ?, ?, ?, ?, ?, ?)}";
        Connection conn = null;
        CallableStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareCall(sql);

            // Set parameters
            stmt.setString(1, user.getUserId());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getCourse());
            stmt.setString(5, user.getDepartment());
            stmt.setObject(6, user.getYear(), Types.INTEGER);
            stmt.setString(7, adminId);

            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Database update error: " + e.getMessage());
            return false;
        } finally {
            try {
                DatabaseConnection.closeResources(conn, stmt, null);
            } catch (Exception e) {
                System.err.println("Resource cleanup error: " + e.getMessage());
            }
        }
    }

    public boolean deleteUser(String userId, String requesterId) {
        String sql = "{CALL DeleteUser(?, ?)}";
        Connection conn = null;
        CallableStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareCall(sql);
            stmt.setString(1, userId);
            stmt.setString(2, requesterId);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Database error deleting user: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, stmt, null);
        }
    }
}
