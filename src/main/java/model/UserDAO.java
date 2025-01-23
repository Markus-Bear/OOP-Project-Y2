package model;

import model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    //Fetch all Users
    public List<User> getAllUsers() {
        String query = "SELECT u.user_id, u.email, u.password, u.role, " +
                "       CASE " +
                "           WHEN u.role = 'Student' THEN s.name " +
                "           WHEN u.role = 'Lecturer' THEN l.name " +
                "           WHEN u.role = 'Admin' THEN a.name " +
                "           WHEN u.role = 'MediaStaff' THEN m.name " +
                "           ELSE NULL " +
                "       END AS name, " +
                "       s.course, s.department AS student_department, s.year, " +
                "       l.department AS lecturer_department " +
                "FROM Users u " +
                "LEFT JOIN Students s ON u.user_id = s.student_id " +
                "LEFT JOIN Lecturers l ON u.user_id = l.lecturer_id " +
                "LEFT JOIN Admins a ON u.user_id = a.admin_id " +
                "LEFT JOIN MediaStaff m ON u.user_id = m.media_staff_id";

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
                user.setPassword(rs.getString("password"));
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
}
