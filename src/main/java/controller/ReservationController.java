package controller;

import exception.DatabaseOperationException;
import exception.RoleAccessException;
import model.Reservation;
import model.ReservationDAO;
import model.UserDAO;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Controller for handling reservation-related operations.
 * Provides methods for users to request reservations and for admins/media staff to view or update reservations.
 * <p>
 * This controller encapsulates error handling by catching exceptions, writing details to a log file,
 * and returning generic responses (or empty lists) so that the GUI only needs to display a default message.
 * </p>
 */
public class ReservationController {
    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final UserDAO userDAO = new UserDAO();
    private static final String LOG_FILE = "error.log";


    /**
     * Allows a user to request a reservation for a piece of equipment.
     * Validates that the user has the appropriate role ("Student" or "Lecturer").
     *
     * @param userId          the ID of the user requesting the reservation.
     * @param equipmentId     the ID of the equipment to reserve.
     * @param reservationDate the date of the reservation.
     * @return true if the reservation is created successfully; false otherwise.
     */
    public boolean requestReservation(String userId, String equipmentId, Date reservationDate) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                throw new IllegalArgumentException("User ID cannot be null or empty.");
            }
            if (equipmentId == null || equipmentId.trim().isEmpty()) {
                throw new IllegalArgumentException("Equipment ID cannot be null or empty.");
            }
            if (reservationDate == null) {
                throw new IllegalArgumentException("Reservation date cannot be null.");
            }

            String role = userDAO.getUserRole(userId);
            RoleValidator.validateRole(role, "Student", "Lecturer");

            return reservationDAO.createReservation(userId, equipmentId, reservationDate);
        } catch (IllegalArgumentException | RoleAccessException | DatabaseOperationException ex) {
            logError("Error in requestReservation", ex);
            // Return false to indicate the reservation request failed.
            return false;
        }
    }

    /**
     * Retrieves all reservations.
     * For Admin and Media Staff, all reservations are returned;
     * for regular users, only their own reservations are returned.
     *
     * @param userId the ID of the user making the request.
     * @return a list of reservations; returns an empty list if an error occurs.
     */
    public List<Reservation> getAllReservations(String userId) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                throw new IllegalArgumentException("User ID cannot be null or empty.");
            }
            String role = userDAO.getUserRole(userId);
            boolean isAdminOrMediaStaff = role.equalsIgnoreCase("Admin") || role.equalsIgnoreCase("MediaStaff");
            return reservationDAO.getAllReservations(userId, isAdminOrMediaStaff);
        } catch (IllegalArgumentException | DatabaseOperationException ex) {
            logError("Error in getAllReservations", ex);
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves reservations for a specific user.
     *
     * @param userId the ID of the user.
     * @return a list of reservations for the user; returns an empty list if an error occurs.
     */
    public List<Reservation> getUserReservations(String userId) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                throw new IllegalArgumentException("User ID cannot be null or empty.");
            }
            return reservationDAO.getAllReservations(userId, false);
        } catch (IllegalArgumentException | DatabaseOperationException ex) {
            logError("Error in getUserReservations", ex);
            return new ArrayList<>();
        }
    }

    /**
     * Allows an admin or media staff to approve or reject a reservation.
     *
     * @param reservationId the ID of the reservation to update.
     * @param status        the new status for the reservation (e.g., "Approved" or "Rejected").
     * @param staffId       the staff member's ID performing the update.
     * @return true if the update is successful; false otherwise.
     */
    public boolean updateReservationStatus(int reservationId, String status, String staffId) {
        try {
            if (reservationId <= 0) {
                throw new IllegalArgumentException("Reservation ID must be greater than 0.");
            }
            if (status == null || status.trim().isEmpty()) {
                throw new IllegalArgumentException("Status cannot be null or empty.");
            }
            if (staffId == null || staffId.trim().isEmpty()) {
                throw new IllegalArgumentException("Staff ID cannot be null or empty.");
            }

            String adminRole = userDAO.getUserRole(staffId);
            RoleValidator.validateRole(adminRole, "Admin", "MediaStaff");

            return reservationDAO.approveReservation(reservationId, staffId, status);
        } catch (IllegalArgumentException | RoleAccessException | DatabaseOperationException ex) {
            logError("Error in updateReservationStatus", ex);
            return false;
        }
    }

    /**
     * Logs detailed error information to a log file.
     *
     * @param message the error message to log.
     * @param ex      the exception to log.
     */
    private void logError(String message, Exception ex) {
        try (PrintWriter out = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            out.println("[" + java.time.LocalDateTime.now() + "] " + message);
            ex.printStackTrace(out);
        } catch (IOException ioEx) {
            System.err.println("Failed to write to log file: " + ioEx.getMessage());
        }
    }
}
