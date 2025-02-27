package controller;

import model.CheckoutDAO;
import exception.DatabaseOperationException;
import java.util.List;
import java.util.ArrayList;

/**
 * Provides operations for checking out and checking in equipment.
 * This controller wraps CheckoutDAO methods and handles any database errors.
 */
public class CheckoutController {
    private final CheckoutDAO checkoutDAO = new CheckoutDAO();

    /**
     * Retrieves a list of pending checkouts.
     *
     * @return a list of pending checkout details as strings, or an empty list if an error occurs.
     */
    public List<String> getPendingCheckouts() {
        try {
            return checkoutDAO.getPendingCheckouts();
        } catch (DatabaseOperationException e) {
            System.err.println("Database error retrieving pending checkouts: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Processes the check-out of equipment for a given reservation.
     *
     * @param reservationId the ID of the reservation to check out
     * @param staffId       the staff ID performing the check-out
     * @return true if the check-out is successful; false otherwise.
     */
    public boolean checkOutEquipment(int reservationId, String staffId) {
        try {
            return checkoutDAO.checkOutEquipment(reservationId, staffId);
        } catch (DatabaseOperationException e) {
            System.err.println("Database error during equipment checkout: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a list of checked-out equipment details.
     *
     * @return a list of checked-out equipment as strings, or an empty list if an error occurs.
     */
    public List<String> getCheckedOutEquipment() {
        try {
            return checkoutDAO.getCheckedOutEquipment();
        } catch (DatabaseOperationException e) {
            System.err.println("Database error retrieving checked-out equipment: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Processes the check-in of equipment.
     *
     * @param reservationId  the reservation ID associated with the equipment to be checked in
     * @param staffId        the staff ID performing the check-in
     * @param equipmentState the state of the equipment (e.g., Good, Fair, Poor)
     * @return true if the check-in is successful; false otherwise.
     */
    public boolean checkInEquipment(int reservationId, String staffId, String equipmentState) {
        try {
            return checkoutDAO.checkInEquipment(reservationId, staffId, equipmentState);
        } catch (DatabaseOperationException e) {
            System.err.println("Database error during equipment check-in: " + e.getMessage());
            return false;
        }
    }
}
