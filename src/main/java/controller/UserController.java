package controller;

import exception.AuthenticationException;
import exception.DatabaseOperationException;
import exception.RoleAccessException;
import model.User;
import model.UserDAO;
import java.util.List;
import java.util.ArrayList;

/**
 * The UserController class provides methods to manage user-related operations such as authentication,
 * retrieval, addition, update, and deletion of users.
 */
public class UserController {
    private final UserDAO userDAO = new UserDAO();

    /**
     * Authenticates a user with the provided email and password.
     *
     * @param email    the user's email address
     * @param password the user's password
     * @return a User object if authentication is successful
     * @throws AuthenticationException    if authentication fails
     * @throws DatabaseOperationException if a database error occurs during authentication
     */
    public User login(String email, String password) throws AuthenticationException, DatabaseOperationException {
        return userDAO.authenticateUser(email, password);
    }

    /**
     * Retrieves all users in the system. This method is restricted to Admin users.
     *
     * @param requesterRole the role of the requester; must be "Admin"
     * @return a list of all users if the requester is an Admin; otherwise, returns an empty list
     * @throws DatabaseOperationException if a database error occurs during retrieval
     */
    public List<User> getAllUsers(String requesterRole) throws DatabaseOperationException {
        try {
            RoleValidator.validateRole(requesterRole, "Admin");
            return userDAO.getAllUsers();
        } catch (RoleAccessException e) {
            System.err.println("Access Denied: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all users with roles Lecturer or Student.
     *
     * @return a list of users who are either Lecturers or Students
     * @throws DatabaseOperationException if a database error occurs during retrieval
     */
    public List<User> getLecturersAndStudents() throws DatabaseOperationException {
        return userDAO.getLecturersAndStudents();
    }

    /**
     * Retrieves a user based on the provided user ID.
     *
     * @param userId the user ID
     * @return the User object if found, or null otherwise
     * @throws DatabaseOperationException if a database error occurs during retrieval
     */
    public User getUserById(String userId) throws DatabaseOperationException {
        return userDAO.getUserById(userId);
    }

    /**
     * Adds a new user to the system. Only an Admin can perform this operation.
     *
     * @param newUser the User object containing the new user's details
     * @param adminId the Admin's ID used for verification
     * @return true if the user is added successfully, false otherwise
     */
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

    /**
     * Updates the information of an existing user.
     *
     * @param updatedUser the User object containing the updated information
     * @param adminId     the Admin's ID used for verification
     * @return true if the update is successful, false otherwise
     */
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

    /**
     * Deletes a user from the system.
     *
     * @param userId  the ID of the user to delete
     * @param adminId the Admin's ID used for verification
     * @return true if the deletion is successful, false otherwise
     */
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
