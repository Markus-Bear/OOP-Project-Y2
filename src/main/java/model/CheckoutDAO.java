package model;

import exception.DatabaseOperationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for performing checkout-related operations,
 * including fetching pending and checked-out equipment, checking out equipment,
 * and processing check-ins.
 */
public class CheckoutDAO {

    /**
     * Retrieves a list of pending reservations that are approved but not yet checked out.
     * The returned list contains reservation details formatted as a string.
     *
     * @return a list of pending checkout details
     * @throws DatabaseOperationException if a database error occurs
     */
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

    /**
     * Checks out equipment by calling the stored procedure {@code CheckOutEquipment}.
     * This method registers an OUT parameter for the number of affected rows.
     *
     * @param reservationId the reservation ID to check out
     * @param staffId       the staff ID performing the checkout
     * @return true if the checkout was successful; false otherwise
     * @throws DatabaseOperationException if a database error occurs
     * @throws IllegalArgumentException   if reservationId is less than or equal to 0 or if staffId is null or empty
     */
    public boolean checkOutEquipment(int reservationId, String staffId) throws DatabaseOperationException {
        if (reservationId <= 0) {
            throw new IllegalArgumentException("Reservation ID must be greater than 0.");
        }
        if (staffId == null || staffId.trim().isEmpty()) {
            throw new IllegalArgumentException("Staff ID cannot be null or empty.");
        }

        String sql = "{CALL CheckOutEquipment(?, ?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, reservationId);
            stmt.setString(2, staffId);
            stmt.registerOutParameter(3, Types.INTEGER);

            stmt.executeUpdate();
            int rowsAffected = stmt.getInt(3);

            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error during check-out process.", e);
        }
    }

    /**
     * Processes the check-in of equipment by calling the stored procedure {@code ReturnCheckedOutEquipment}.
     * The stored procedure takes the reservation ID, staff ID, and equipment state as parameters and
     * returns the number of affected rows as an OUT parameter.
     *
     * @param reservationId  the reservation ID of the checked-out equipment
     * @param staffId        the staff ID performing the check-in
     * @param equipmentState the state of the equipment (e.g., Good, Fair, Poor)
     * @return true if the check-in was successful; false otherwise
     * @throws DatabaseOperationException if a database error occurs
     * @throws IllegalArgumentException   if reservationId is less than or equal to 0, staffId is null/empty, or equipmentState is null/empty
     */
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

        String sql = "{CALL ReturnCheckedOutEquipment(?, ?, ?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, reservationId);
            stmt.setString(2, staffId);
            stmt.setString(3, equipmentState);
            stmt.registerOutParameter(4, Types.INTEGER);

            stmt.executeUpdate();
            int rowsAffected = stmt.getInt(4);

            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error during check-in process.", e);
        }
    }

    /**
     * Retrieves a list of equipment that has been checked out (i.e., has a checkout record with no check-in date).
     * The returned list contains details formatted as a string.
     *
     * @return a list of checked-out equipment details.
     * @throws DatabaseOperationException if a database error occurs.
     */
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
