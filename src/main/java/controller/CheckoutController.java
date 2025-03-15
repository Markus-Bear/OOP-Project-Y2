package controller;

import model.CheckoutDAO;
import exception.DatabaseOperationException;
import java.util.List;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Provides operations for checking out and checking in equipment.
 * This controller wraps CheckoutDAO methods, logs detailed error information to a log file,
 * and returns generic error responses so that the GUI displays only default error messages.
 */
public class CheckoutController {
    private final CheckoutDAO checkoutDAO = new CheckoutDAO();
    private static final String LOG_FILE = "checkout_errors.log";

    /**
     * Retrieves a list of pending checkouts.
     *
     * @return a list of pending checkout details as strings; if an error occurs, returns an empty list.
     */
    public List<String> getPendingCheckouts() {
        try {
            return checkoutDAO.getPendingCheckouts();
        } catch (DatabaseOperationException e) {
            logError("Error retrieving pending checkouts", e);
            return new ArrayList<>();
        }
    }

    /**
     * Processes the check-out of equipment for a given reservation.
     *
     * @param reservationId the ID of the reservation to check out.
     * @param staffId       the staff member's ID performing the check-out.
     * @return true if the check-out is successful; false otherwise.
     */
    public boolean checkOutEquipment(int reservationId, String staffId) {
        try {
            return checkoutDAO.checkOutEquipment(reservationId, staffId);
        } catch (DatabaseOperationException e) {
            logError("Error during equipment checkout", e);
            return false;
        }
    }

    /**
     * Retrieves a list of checked-out equipment details.
     *
     * @return a list of checked-out equipment details as strings; if an error occurs, returns an empty list.
     */
    public List<String> getCheckedOutEquipment() {
        try {
            return checkoutDAO.getCheckedOutEquipment();
        } catch (DatabaseOperationException e) {
            logError("Error retrieving checked-out equipment", e);
            return new ArrayList<>();
        }
    }

    /**
     * Processes the check-in of equipment.
     *
     * @param reservationId  the reservation ID associated with the equipment to be checked in.
     * @param staffId        the staff member's ID performing the check-in.
     * @param equipmentState the state of the equipment (e.g., Good, Fair, Poor).
     * @return true if the check-in is successful; false otherwise.
     */
    public boolean checkInEquipment(int reservationId, String staffId, String equipmentState) {
        try {
            return checkoutDAO.checkInEquipment(reservationId, staffId, equipmentState);
        } catch (DatabaseOperationException e) {
            logError("Error during equipment check-in", e);
            return false;
        }
    }

    /**
     * Logs detailed error information to a log file.
     *
     * @param message a message describing the context of the error.
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
