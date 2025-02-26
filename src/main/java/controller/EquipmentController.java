package controller;

import model.Equipment;
import model.EquipmentDAO;
import model.UserDAO;
import exception.RoleAccessException;
import java.util.ArrayList;
import java.util.List;

public class EquipmentController {
    private final EquipmentDAO equipmentDAO = new EquipmentDAO();
    private final UserDAO userDAO = new UserDAO();

    // Retrieve all equipment
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

    // Retrieve equipment by type
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

    public List<Equipment> getEquipmentByStatus(String status) {
        try {
            return equipmentDAO.getEquipmentByStatus(status);
        } catch (Exception e) {
            System.err.println("Error retrieving equipment by status: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Equipment> getEquipmentByTypeAndStatus(String type, String status) {
        try {
            return equipmentDAO.getEquipmentByTypeAndStatus(type, status);
        } catch (Exception e) {
            System.err.println("Error retrieving equipment by type and status: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Add Equipment
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

    // Update equipment
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

    // Delete equipment
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
