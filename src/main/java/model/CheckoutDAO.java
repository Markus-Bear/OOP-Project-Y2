package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import exception.DatabaseOperationException;


public class CheckoutDAO {

    // Fetch reservations that are approved but not yet checked out
    public List<String> getPendingCheckouts() throws DatabaseOperationException {
        List<String> pendingReservations = new ArrayList<>();
        String query = """
            SELECT r.reservation_id, u.name AS user_name, e.name AS equipment_name, r.reservation_date
            FROM Reservations r
            JOIN Users u ON r.user_id = u.user_id
            JOIN Equipment e ON r.equipment_id = e.equipment_id
            LEFT JOIN Checkouts c ON r.reservation_id = c.reservation_id
            WHERE r.status = 'Approved' AND c.reservation_id IS NULL
            ORDER BY r.reservation_date ASC
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int reservationId = rs.getInt("reservation_id");
                String userName = rs.getString("user_name");
                String equipmentName = rs.getString("equipment_name");
                Date reservationDate = rs.getDate("reservation_date");

                pendingReservations.add(reservationId + " | " + userName + " | " + equipmentName + " | " + reservationDate);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching pending checkouts.", e);
        }

        return pendingReservations;
    }

    // Existing method to check out equipment
    public boolean checkOutEquipment(int reservationId, String staffId) throws DatabaseOperationException {
        if (reservationId <= 0) {
            throw new IllegalArgumentException("Reservation ID must be greater than 0.");
        }
        if (staffId == null || staffId.trim().isEmpty()) {
            throw new IllegalArgumentException("Staff ID cannot be null or empty.");
        }

        String sql = "{CALL CheckOutEquipment(?, ?, ?)}"; // Stored procedure with OUT parameter

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, reservationId);
            stmt.setString(2, staffId);
            stmt.registerOutParameter(3, java.sql.Types.INTEGER); // Register OUT parameter

            stmt.executeUpdate();
            int rowsAffected = stmt.getInt(3); // Get affected rows count

            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error during check-out process.", e);
        }
    }

    // Check-in equipment by calling the stored procedure `ReturnCheckedOutEquipment`
    public boolean checkInEquipment(int reservationId, String staffId, String equipmentState) throws DatabaseOperationException {
        if (reservationId <= 0) {
            throw new IllegalArgumentException("Reservation ID must be greater than 0.");
        }
        if (staffId == null || staffId.trim().isEmpty()) {
            throw new IllegalArgumentException("Staff ID cannot be null or empty.");
        }
        if (equipmentState == null || equipmentState.trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment state cannot be null or empty.");
        }

        String sql = "{CALL ReturnCheckedOutEquipment(?, ?, ?, ?)}"; // Stored procedure with equipmentState parameter

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, reservationId);
            stmt.setString(2, staffId);
            stmt.setString(3, equipmentState);
            stmt.registerOutParameter(4, java.sql.Types.INTEGER); // OUT parameter for affected rows

            stmt.executeUpdate();
            int rowsAffected = stmt.getInt(4);

            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error during check-in process.", e);
        }
    }

    // Fetch checked-out equipment for check-in
    public List<String> getCheckedOutEquipment() throws DatabaseOperationException {
        List<String> checkedOutList = new ArrayList<>();
        String query = """
            SELECT r.reservation_id, u.name AS user_name, e.name AS equipment_name, c.checked_out_date
            FROM Reservations r
            JOIN Users u ON r.user_id = u.user_id
            JOIN Equipment e ON r.equipment_id = e.equipment_id
            JOIN Checkouts c ON r.reservation_id = c.reservation_id
            WHERE e.status = 'CheckedOut' AND c.check_in_date IS NULL
            ORDER BY c.checked_out_date ASC
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int reservationId = rs.getInt("reservation_id");
                String userName = rs.getString("user_name");
                String equipmentName = rs.getString("equipment_name");
                Timestamp checkedOutDate = rs.getTimestamp("checked_out_date");

                checkedOutList.add(reservationId + " | " + userName + " | " + equipmentName + " | " + checkedOutDate);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching checked-out equipment.", e);
        }

        return checkedOutList;
    }
}
