package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import exception.DatabaseOperationException;

public class ReservationDAO {

    // Fetch all reservations (Admin & Media Staff see all, regular users see their own)
    public List<Reservation> getAllReservations(String userId, boolean isAdminOrMediaStaff) throws DatabaseOperationException {
        if (!isAdminOrMediaStaff && (userId == null || userId.trim().isEmpty())) {
            throw new IllegalArgumentException("User ID cannot be null or empty for non-admin users.");
        }

        List<Reservation> reservations = new ArrayList<>();

        String query = isAdminOrMediaStaff
                ? "SELECT r.reservation_id, u.name AS user_name, e.name AS equipment_name, r.reservation_date, r.return_date, r.status " +
                "FROM Reservations r " +
                "JOIN Users u ON r.user_id = u.user_id " +
                "JOIN Equipment e ON r.equipment_id = e.equipment_id"
                : "SELECT r.reservation_id, u.name AS user_name, e.name AS equipment_name, r.reservation_date, r.return_date, r.status " +
                "FROM Reservations r " +
                "JOIN Users u ON r.user_id = u.user_id " +
                "JOIN Equipment e ON r.equipment_id = e.equipment_id " +
                "WHERE r.user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (!isAdminOrMediaStaff) {
                stmt.setString(1, userId); // Only apply filter for non-admin users
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(new Reservation(
                            rs.getInt("reservation_id"),
                            rs.getString("user_name"),
                            rs.getString("equipment_name"),
                            rs.getDate("reservation_date"),
                            rs.getDate("return_date"),
                            rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching reservations from the database.", e);
        }

        return reservations;
    }

    // Create a reservation request
    public boolean createReservation(String userId, String equipmentId, Date reservationDate) throws DatabaseOperationException {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        if (equipmentId == null || equipmentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment ID cannot be null or empty.");
        }
        if (reservationDate == null) {
            throw new IllegalArgumentException("Reservation date cannot be null.");
        }

        String query = "INSERT INTO reservations (user_id, equipment_id, reservation_date, status) VALUES (?, ?, ?, 'Pending')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userId);
            stmt.setString(2, equipmentId);
            stmt.setDate(3, reservationDate);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error creating reservation for user ID: " + userId, e);
        }
    }

    // Approve or reject a reservation (Admin only)
    public boolean approveReservation(int reservationId, String adminId, String status) throws DatabaseOperationException {
        if (reservationId <= 0) {
            throw new IllegalArgumentException("Reservation ID must be greater than 0.");
        }
        if (adminId == null || adminId.trim().isEmpty()) {
            throw new IllegalArgumentException("Admin ID cannot be null or empty.");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty.");
        }

        String query = "UPDATE reservations SET status = ? WHERE reservation_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status);
            stmt.setInt(2, reservationId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating reservation status for reservation ID: " + reservationId, e);
        }
    }
}
