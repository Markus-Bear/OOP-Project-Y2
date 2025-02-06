package controller;

import model.User;
import model.UserDAO;
import java.util.List;

public class UserController {
    private final UserDAO userDAO = new UserDAO();

    // Retrieve all users (Admin only)
    public List<User> getAllUsers(String requesterRole)  {
        RoleValidator.validateRole(requesterRole, "Admin");
        return userDAO.getAllUsers();
    }

    public User getUserById(String userId) {
        return userDAO.getUserById(userId);
    }

    //Add Users
    public boolean addUser(User newUser, String adminId) {
        try {
            String adminRole = userDAO.getUserRole(adminId);
            RoleValidator.validateRole(adminRole, "Admin");
            return userDAO.addUser(newUser, adminId);
        } catch (Exception e) {
            System.err.println("Add failed: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUser(User updatedUser, String adminId) {
        try {
            return userDAO.updateUser(updatedUser, adminId); // Pass adminId to DAO
        } catch (Exception e) {
            System.err.println("Update error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(String userId, String adminId) {
        try {
            String adminRole = userDAO.getUserRole(adminId);
            RoleValidator.validateRole(adminRole, "Admin");
            return userDAO.deleteUser(userId, adminId);
        } catch (Exception e) {
            System.err.println("Delete failed: " + e.getMessage());
            return false;
        }
    }
}

