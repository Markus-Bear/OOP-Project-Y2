package controller;

import exception.AuthenticationException;
import exception.DatabaseOperationException;
import exception.RoleAccessException;
import model.User;
import model.UserDAO;
import java.util.List;
import java.util.ArrayList;

public class UserController {
    private final UserDAO userDAO = new UserDAO();

    public User login(String email, String password) throws AuthenticationException, DatabaseOperationException {
        return userDAO.authenticateUser(email, password);
    }

    // Retrieve all users (Admin only)
    public List<User> getAllUsers(String requesterRole) throws DatabaseOperationException {
        try {
            RoleValidator.validateRole(requesterRole, "Admin");
            return userDAO.getAllUsers();
        } catch (RoleAccessException e) {
            System.err.println("Access Denied: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<User> getLecturersAndStudents() throws DatabaseOperationException {
        return userDAO.getLecturersAndStudents();
    }

    public User getUserById(String userId) throws DatabaseOperationException {
        return userDAO.getUserById(userId);
    }

    // Add Users
    public boolean addUser(User newUser, String adminId) {
        try {
            String adminRole = userDAO.getUserRole(adminId);
            RoleValidator.validateRole(adminRole, "Admin");
            return userDAO.addUser(newUser, adminId);
        } catch (RoleAccessException e) {
            System.err.println("Access Denied: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return false;
    }

    public boolean updateUser(User updatedUser, String adminId) {
        try {
            return userDAO.updateUser(updatedUser, adminId);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteUser(String userId, String adminId) {
        try {
            String adminRole = userDAO.getUserRole(adminId);
            RoleValidator.validateRole(adminRole, "Admin");
            return userDAO.deleteUser(userId, adminId);
        } catch (RoleAccessException e) {
            System.err.println("Access Denied: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return false;
    }
}
