package controller;

import exception.DatabaseOperationException;
import exception.RoleAccessException;
import model.Reservation;
import model.ReservationDAO;
import model.UserDAO;
import java.sql.Date;
import java.util.List;

/**
 * Controller for handling reservation-related operations.
 * Provides methods for users to request reservations and for admins/media staff to view or update reservations.
 */
public class ReservationController {
    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final UserDAO userDAO = new UserDAO();

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
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (RoleAccessException e) {
            System.err.println("Authorization error: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Retrieves all reservations.
     * For Admin and Media Staff, all reservations are returned;
     * for regular users, only their own reservations are returned.
     *
     * @param userId the ID of the user making the request.
     * @return a list of reservations.
     */
    public List<Reservation> getAllReservations(String userId) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                throw new IllegalArgumentException("User ID cannot be null or empty.");
            }

            String role = userDAO.getUserRole(userId);
            boolean isAdminOrMediaStaff = role.equalsIgnoreCase("Admin") || role.equalsIgnoreCase("MediaStaff");

            return reservationDAO.getAllReservations(userId, isAdminOrMediaStaff);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return List.of();
    }

    /**
     * Retrieves reservations for a specific user.
     *
     * @param userId the ID of the user.
     * @return a list of reservations for the user.
     */
    public List<Reservation> getUserReservations(String userId) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                throw new IllegalArgumentException("User ID cannot be null or empty.");
            }
            return reservationDAO.getAllReservations(userId, false);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return List.of();
    }

    /**
     * Allows an admin to approve or reject a reservation.
     *
     * @param reservationId the ID of the reservation to update.
     * @param status        the new status for the reservation.
     * @param adminId       the admin's ID performing the update.
     * @return true if the update is successful; false otherwise.
     */
    public boolean updateReservationStatus(int reservationId, String status, String adminId) {
        try {
            if (reservationId <= 0) {
                throw new IllegalArgumentException("Reservation ID must be greater than 0.");
            }
            if (status == null || status.trim().isEmpty()) {
                throw new IllegalArgumentException("Status cannot be null or empty.");
            }
            if (adminId == null || adminId.trim().isEmpty()) {
                throw new IllegalArgumentException("Admin ID cannot be null or empty.");
            }

            String adminRole = userDAO.getUserRole(adminId);
            RoleValidator.validateRole(adminRole, "Admin");

            return reservationDAO.approveReservation(reservationId, adminId, status);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (RoleAccessException e) {
            System.err.println("Authorization error: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return false;
    }
}
