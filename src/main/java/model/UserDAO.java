package model;

import model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import exception.AuthenticationException;
import exception.DatabaseOperationException;

public class UserDAO {


    public User authenticateUser(String email, String password) throws AuthenticationException, DatabaseOperationException {
        String query = "SELECT u.user_id, u.email, u.name, u.role, " +
                "s.course, s.department AS student_department, s.year, " +
                "l.department AS lecturer_department " +
                "FROM users u " +
                "LEFT JOIN students s ON u.user_id = s.student_id " +
                "LEFT JOIN lecturers l ON u.user_id = l.lecturer_id " +
                "WHERE u.email = ? AND u.password = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password); // In a real system, passwords should be hashed!
            rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getString("user_id"));
                user.setEmail(rs.getString("email"));
                user.setName(rs.getString("name"));
                user.setRole(rs.getString("role"));

                if ("Student".equalsIgnoreCase(user.getRole())) {
                    user.setCourse(rs.getString("course"));
                    user.setDepartment(rs.getString("student_department"));
                    user.setYear(rs.getObject("year") != null ? rs.getInt("year") : null);
                } else if ("Lecturer".equalsIgnoreCase(user.getRole())) {
                    user.setDepartment(rs.getString("lecturer_department"));
                }

                return user;
            } else {
                throw new AuthenticationException("Invalid email or password.");
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error during authentication.", e);
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
    }



    //Fetch all Users
    public List<User> getAllUsers() throws DatabaseOperationException {
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

                if ("Student".equalsIgnoreCase(user.getRole())) {
                    user.setCourse(rs.getString("course"));
                    user.setDepartment(rs.getString("student_department"));
                    user.setYear(rs.getObject("year") != null ? rs.getInt("year") : null);
                } else if ("Lecturer".equalsIgnoreCase(user.getRole())) {
                    user.setDepartment(rs.getString("lecturer_department"));
                }

                users.add(user);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching all users from the database.", e);
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }

        return users;
    }


    public List<User> getLecturersAndStudents() throws DatabaseOperationException {
        String query = "SELECT user_id, email, name, role FROM users WHERE role IN ('Lecturer', 'Student')";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<User> lecturersAndStudents = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getString("user_id"));
                user.setEmail(rs.getString("email"));
                user.setName(rs.getString("name"));
                user.setRole(rs.getString("role"));
                lecturersAndStudents.add(user);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching lecturers and students from the database.", e);
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }

        return lecturersAndStudents;
    }


    public User getUserById(String userId) throws DatabaseOperationException {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }

        String query = "SELECT u.user_id, u.email, u.name, u.role, " +
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

                //Properly set role-specific fields
                if ("Student".equalsIgnoreCase(user.getRole())) {
                    user.setCourse(rs.getString("course"));
                    user.setDepartment(rs.getString("student_department"));
                    user.setYear(rs.getObject("year") != null ? rs.getInt("year") : null); // Handles NULL year values
                } else if ("Lecturer".equalsIgnoreCase(user.getRole())) {
                    user.setDepartment(rs.getString("lecturer_department"));
                }

                return user;
            } else {
                return null; // User not found
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching user by ID: " + userId, e);
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
    }


    public String getUserRole(String userId) throws DatabaseOperationException {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }

        String query = "SELECT role FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching user role for ID: " + userId, e);
        }

        return null; // User ID not found, returning null
    }

    public boolean addUser(User user, String creatorId) throws DatabaseOperationException {
        if (user == null) {
            throw new IllegalArgumentException("User object cannot be null.");
        }
        if (creatorId == null || creatorId.trim().isEmpty()) {
            throw new IllegalArgumentException("Creator ID cannot be null or empty.");
        }

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
            throw new DatabaseOperationException("Error adding user to the database.", e);
        } finally {
            // Ensuring resources are closed even if an exception occurs
            try {
                DatabaseConnection.closeResources(conn, stmt);
            } catch (Exception e) {
                System.err.println("Resource cleanup error: " + e.getMessage());
            }
        }
    }

    public boolean updateUser(User user, String adminId) throws DatabaseOperationException {
        if (user == null) {
            throw new IllegalArgumentException("User object cannot be null.");
        }
        if (adminId == null || adminId.trim().isEmpty()) {
            throw new IllegalArgumentException("Admin ID cannot be null or empty.");
        }
        if (user.getUserId() == null || user.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }

        String sql = "{CALL UpdateUser(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            // Set parameters
            stmt.setString(1, user.getUserId());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getName());

            // Handle potential NULL values properly
            if (user.getCourse() != null) {
                stmt.setString(4, user.getCourse());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }

            if (user.getDepartment() != null) {
                stmt.setString(5, user.getDepartment());
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }

            if (user.getYear() != null) {
                stmt.setInt(6, user.getYear());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            stmt.setString(7, adminId);

            stmt.execute();
            return true;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating user with ID: " + user.getUserId(), e);
        }
    }

    public boolean deleteUser(String userId, String requesterId) throws DatabaseOperationException {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        if (requesterId == null || requesterId.trim().isEmpty()) {
            throw new IllegalArgumentException("Requester ID cannot be null or empty.");
        }

        String sql = "{CALL DeleteUser(?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, userId);
            stmt.setString(2, requesterId);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting user with ID: " + userId, e);
        }
    }
}
