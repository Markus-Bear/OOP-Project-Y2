package controller;

import model.CheckoutDAO;
import exception.DatabaseOperationException;
import java.util.List;
import java.util.ArrayList;

public class CheckoutController {
    private final CheckoutDAO checkoutDAO = new CheckoutDAO();

    public List<String> getPendingCheckouts() {
        try {
            return checkoutDAO.getPendingCheckouts();
        } catch (DatabaseOperationException e) {
            System.err.println("Database error retrieving pending checkouts: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean checkOutEquipment(int reservationId, String staffId) {
        try {
            return checkoutDAO.checkOutEquipment(reservationId, staffId);
        } catch (DatabaseOperationException e) {
            System.err.println("Database error during equipment checkout: " + e.getMessage());
            return false;
        }
    }

    public List<String> getCheckedOutEquipment() {
        try {
            return checkoutDAO.getCheckedOutEquipment();
        } catch (DatabaseOperationException e) {
            System.err.println("Database error retrieving checked-out equipment: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean checkInEquipment(int reservationId, String staffId, String equipmentState) {
        try {
            return checkoutDAO.checkInEquipment(reservationId, staffId, equipmentState);
        } catch (DatabaseOperationException e) {
            System.err.println("Database error during equipment check-in: " + e.getMessage());
            return false;
        }
    }
}
