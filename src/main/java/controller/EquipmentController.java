package controller;

import exception.DatabaseOperationException;
import exception.RoleAccessException;
import model.Equipment;
import model.EquipmentDAO;
import model.UserDAO;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Provides high-level operations for managing equipment.
 * It performs role validation and calls methods from EquipmentDAO and UserDAO.
 * <p>
 * This controller encapsulates error handling by logging detailed error information to a log file,
 * and returning generic values so that the GUI only receives a default error response.
 * </p>
 */
public class EquipmentController {
    private final EquipmentDAO equipmentDAO = new EquipmentDAO();
    private final UserDAO userDAO = new UserDAO();
    private static final String LOG_FILE = "equipment_errors.log";

    /**
     * Retrieves all equipment available in the system.
     *
     * @param userRole the role of the requester.
     * @return a list of all equipment; if access is denied or an error occurs, returns an empty list.
     */
    public List<Equipment> getAllEquipment(String userRole) {
        try {
            RoleValidator.validateRole(userRole, "Admin", "MediaStaff", "Student", "Lecturer");
            return equipmentDAO.getAllEquipment();
        } catch (RoleAccessException e) {
            logError("Access denied in getAllEquipment", e);
            return new ArrayList<>();
        } catch (DatabaseOperationException e) {
            logError("Database error in getAllEquipment", e);
            return new ArrayList<>();
        } catch (Exception e) {
            logError("Unexpected error in getAllEquipment", e);
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves equipment filtered by type.
     *
     * @param type     the equipment type to filter by.
     * @param userRole the role of the requester.
     * @return a list of equipment with the specified type; if access is denied or an error occurs, returns an empty list.
     */
    public List<Equipment> getEquipmentByType(String type, String userRole) {
        try {
            RoleValidator.validateRole(userRole, "Admin", "MediaStaff", "Student", "Lecturer");
            return equipmentDAO.getEquipmentByType(type);
        } catch (RoleAccessException e) {
            logError("Access denied in getEquipmentByType", e);
            return new ArrayList<>();
        } catch (DatabaseOperationException e) {
            logError("Database error in getEquipmentByType", e);
            return new ArrayList<>();
        } catch (Exception e) {
            logError("Unexpected error in getEquipmentByType", e);
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves equipment filtered by status.
     *
     * @param status the equipment status to filter by.
     * @return a list of equipment with the specified status; if an error occurs, returns an empty list.
     */
    public List<Equipment> getEquipmentByStatus(String status) {
        try {
            return equipmentDAO.getEquipmentByStatus(status);
        } catch (DatabaseOperationException e) {
            logError("Database error in getEquipmentByStatus", e);
            return new ArrayList<>();
        } catch (Exception e) {
            logError("Unexpected error in getEquipmentByStatus", e);
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves equipment filtered by both type and status.
     *
     * @param type   the equipment type to filter by.
     * @param status the equipment status to filter by.
     * @return a list of equipment matching the specified type and status; if an error occurs, returns an empty list.
     */
    public List<Equipment> getEquipmentByTypeAndStatus(String type, String status) {
        try {
            return equipmentDAO.getEquipmentByTypeAndStatus(type, status);
        } catch (DatabaseOperationException e) {
            logError("Database error in getEquipmentByTypeAndStatus", e);
            return new ArrayList<>();
        } catch (Exception e) {
            logError("Unexpected error in getEquipmentByTypeAndStatus", e);
            return new ArrayList<>();
        }
    }

    /**
     * Adds a new equipment record to the system after validating the user's role.
     *
     * @param newEquipment the Equipment object to add.
     * @param userId       the ID of the user performing the addition.
     * @return true if the equipment is added successfully; false otherwise.
     */
    public boolean addEquipment(Equipment newEquipment, String userId) {
        try {
            String userRole = userDAO.getUserRole(userId);
            if (userRole == null) {
                logError("User role is null in addEquipment", new Exception("User role is null"));
                return false;
            }
            RoleValidator.validateRole(userRole, "Admin", "MediaStaff");
            return equipmentDAO.addEquipment(newEquipment, userId);
        } catch (RoleAccessException e) {
            logError("Access denied in addEquipment", e);
            return false;
        } catch (DatabaseOperationException e) {
            logError("Database error in addEquipment", e);
            return false;
        } catch (Exception e) {
            logError("Unexpected error in addEquipment", e);
            return false;
        }
    }

    /**
     * Updates an existing equipment record after validating the user's role.
     *
     * @param updatedEquipment the Equipment object with updated details.
     * @param userId           the ID of the user performing the update.
     * @return true if the equipment is updated successfully; false otherwise.
     */
    public boolean updateEquipment(Equipment updatedEquipment, String userId) {
        try {
            String userRole = userDAO.getUserRole(userId);
            if (userRole == null) {
                logError("User role is null in updateEquipment", new Exception("User role is null"));
                return false;
            }
            RoleValidator.validateRole(userRole, "Admin", "MediaStaff");
            return equipmentDAO.updateEquipment(updatedEquipment, userId);
        } catch (RoleAccessException e) {
            logError("Access denied in updateEquipment", e);
            return false;
        } catch (DatabaseOperationException e) {
            logError("Database error in updateEquipment", e);
            return false;
        } catch (Exception e) {
            logError("Unexpected error in updateEquipment", e);
            return false;
        }
    }

    /**
     * Deletes an equipment record after validating the user's role.
     *
     * @param equipmentId the ID of the equipment to delete.
     * @param userId      the ID of the user performing the deletion.
     * @return true if the equipment is deleted successfully; false otherwise.
     */
    public boolean deleteEquipment(String equipmentId, String userId) {
        try {
            String userRole = userDAO.getUserRole(userId);
            if (userRole == null) {
                logError("User role is null in deleteEquipment", new Exception("User role is null"));
                return false;
            }
            RoleValidator.validateRole(userRole, "Admin", "MediaStaff");
            return equipmentDAO.deleteEquipment(equipmentId, userId);
        } catch (RoleAccessException e) {
            logError("Access denied in deleteEquipment", e);
            return false;
        } catch (DatabaseOperationException e) {
            logError("Database error in deleteEquipment", e);
            return false;
        } catch (Exception e) {
            logError("Unexpected error in deleteEquipment", e);
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
