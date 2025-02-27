package model;

import exception.DatabaseOperationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for performing reservation-related database operations.
 */
public class ReservationDAO {

    /**
     * Retrieves all reservations.
     * <p>
     * If the user is an admin or media staff, all reservations are returned.
     * Otherwise, only reservations for the specified user ID are returned.
     * </p>
     *
     * @param userId              the user ID to filter reservations (for non-admin users)
     * @param isAdminOrMediaStaff true if the requester is an admin or media staff; false otherwise
     * @return a list of Reservation objects
     * @throws DatabaseOperationException if a database error occurs
     * @throws IllegalArgumentException   if userId is null or empty for non-admin users
     */
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
                stmt.setString(1, userId);
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

    /**
     * Creates a new reservation request.
     *
     * @param userId          the ID of the user making the reservation
     * @param equipmentId     the ID of the equipment to reserve
     * @param reservationDate the date of the reservation
     * @return true if the reservation is created successfully; false otherwise
     * @throws DatabaseOperationException if a database error occurs
     * @throws IllegalArgumentException   if any parameter is null or empty
     */
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

    /**
     * Approves or rejects a reservation. (Admin only)
     *
     * @param reservationId the ID of the reservation to update
     * @param adminId       the admin's ID performing the update
     * @param status        the new status for the reservation
     * @return true if the update is successful; false otherwise
     * @throws DatabaseOperationException if a database error occurs
     * @throws IllegalArgumentException   if reservationId is not greater than 0, or if adminId/status is null or empty
     */
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
