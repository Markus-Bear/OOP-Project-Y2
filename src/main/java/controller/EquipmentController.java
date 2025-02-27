package controller;

import exception.RoleAccessException;
import model.Equipment;
import model.EquipmentDAO;
import model.UserDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides high-level operations for managing equipment.
 * It performs role validation and calls methods from EquipmentDAO and UserDAO.
 */
public class EquipmentController {
    private final EquipmentDAO equipmentDAO = new EquipmentDAO();
    private final UserDAO userDAO = new UserDAO();

    /**
     * Retrieves all equipment available in the system.
     *
     * @param userRole the role of the requester
     * @return a list of all equipment; if access is denied or an error occurs, returns an empty list.
     */
    public List<Equipment> getAllEquipment(String userRole) {
        try {
            RoleValidator.validateRole(userRole, "Admin", "MediaStaff", "Student", "Lecturer");
            return equipmentDAO.getAllEquipment();
        } catch (RoleAccessException e) {
            System.err.println("Access Denied: " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error retrieving equipment: " + e.getMessage());
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
            System.err.println("Access Denied: " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error retrieving equipment by type: " + e.getMessage());
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
        } catch (Exception e) {
            System.err.println("Error retrieving equipment by status: " + e.getMessage());
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
        } catch (Exception e) {
            System.err.println("Error retrieving equipment by type and status: " + e.getMessage());
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
                System.err.println("User role is NULL. User may not exist!");
                return false;
            }
            RoleValidator.validateRole(userRole, "Admin", "MediaStaff");
            return equipmentDAO.addEquipment(newEquipment, userId);
        } catch (RoleAccessException e) {
            System.err.println("Access Denied: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Failed to add equipment: " + e.getMessage());
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
                System.err.println("User role is NULL. User may not exist!");
                return false;
            }
            RoleValidator.validateRole(userRole, "Admin", "MediaStaff");
            return equipmentDAO.updateEquipment(updatedEquipment, userId);
        } catch (RoleAccessException e) {
            System.err.println("Access Denied: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Failed to update equipment: " + e.getMessage());
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
                System.err.println("User role is NULL. User may not exist!");
                return false;
            }
            RoleValidator.validateRole(userRole, "Admin", "MediaStaff");
            return equipmentDAO.deleteEquipment(equipmentId, userId);
        } catch (RoleAccessException e) {
            System.err.println("Access Denied: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Failed to delete equipment: " + e.getMessage());
            return false;
        }
    }
}
