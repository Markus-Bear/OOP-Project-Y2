package controller;

import exception.AuthenticationException;
import exception.DatabaseOperationException;
import exception.RoleAccessException;
import model.User;
import model.UserDAO;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * The UserController class provides methods to manage user-related operations such as authentication,
 * retrieval, addition, update, and deletion of users.
 *
 * This implementation encapsulates error handling by catching exceptions thrown by the DAO,
 * logging detailed error information to a log file, and returning generic error responses to the caller.
 */
public class UserController {
    private final UserDAO userDAO = new UserDAO();
    // Log file name
    private static final String LOG_FILE = "error.log";
    // Generic error message to display in the GUI
    private static final String GENERIC_ERROR_MESSAGE = "An unexpected error occurred. Please try again later.";

    /**
     * Authenticates a user with the provided email and password.
     *
     * @param email the user's email address.
     * @param password the user's password.
     * @return the authenticated User object, or null if authentication fails.
     * @throws RuntimeException with a generic error message if the credentials are invalid.
     */
    public User login(String email, String password) {
        try {
            return userDAO.authenticateUser(email, password);
        } catch (AuthenticationException ae) {
            logError(ae);
            // Rethrow as a runtime exception with a generic message
            throw new RuntimeException("Invalid credentials. " + GENERIC_ERROR_MESSAGE);
        } catch (DatabaseOperationException de) {
            logError(de);
            return null;
        }
    }

    /**
     * Retrieves all users in the system. This method is restricted to Admin users.
     *
     * @param requesterRole the role of the requester; must be "Admin".
     * @return a list of all users if successful; otherwise, an empty list.
     */
    public List<User> getAllUsers(String requesterRole) {
        try {
            RoleValidator.validateRole(requesterRole, "Admin");
            return userDAO.getAllUsers();
        } catch (RoleAccessException rae) {
            logError(rae);
            return new ArrayList<>();
        } catch (DatabaseOperationException de) {
            logError(de);
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all users with roles Lecturer or Student.
     *
     * @return a list of users who are either Lecturers or Students; otherwise, an empty list.
     */
    public List<User> getLecturersAndStudents() {
        try {
            return userDAO.getLecturersAndStudents();
        } catch (DatabaseOperationException de) {
            logError(de);
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves a user based on the provided user ID.
     *
     * @param userId the user ID.
     * @return the User object if found; otherwise, null.
     */
    public User getUserById(String userId) {
        try {
            return userDAO.getUserById(userId);
        } catch (DatabaseOperationException de) {
            logError(de);
            return null;
        }
    }

    /**
     * Adds a new user to the system. Only an Admin can perform this operation.
     *
     * @param newUser the User object containing the new user's details.
     * @param adminId the Admin's ID used for verification.
     * @return true if the user is added successfully; false otherwise.
     */
    public boolean addUser(User newUser, String adminId) {
        try {
            String adminRole = userDAO.getUserRole(adminId);
            RoleValidator.validateRole(adminRole, "Admin");
            return userDAO.addUser(newUser, adminId);
        } catch (RoleAccessException | IllegalArgumentException e) {
            logError(e);
            return false;
        } catch (DatabaseOperationException de) {
            logError(de);
            return false;
        }
    }

    /**
     * Updates the information of an existing user.
     *
     * @param updatedUser the User object containing the updated information.
     * @param adminId the Admin's ID used for verification.
     * @return true if the update is successful; false otherwise.
     */
    public boolean updateUser(User updatedUser, String adminId) {
        try {
            return userDAO.updateUser(updatedUser, adminId);
        } catch (IllegalArgumentException e) {
            logError(e);
            return false;
        } catch (DatabaseOperationException de) {
            logError(de);
            return false;
        }
    }

    /**
     * Deletes a user from the system.
     *
     * @param userId the ID of the user to delete.
     * @param adminId the Admin's ID used for verification.
     * @return true if the deletion is successful; false otherwise.
     */
    public boolean deleteUser(String userId, String adminId) {
        try {
            String adminRole = userDAO.getUserRole(adminId);
            RoleValidator.validateRole(adminRole, "Admin");
            return userDAO.deleteUser(userId, adminId);
        } catch (RoleAccessException | IllegalArgumentException e) {
            logError(e);
            return false;
        } catch (DatabaseOperationException de) {
            logError(de);
            return false;
        }
    }

    /**
     * Logs the provided exception details to a log file using a PrintWriter.
     *
     * @param e the exception to log.
     */
    private void logError(Exception e) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            pw.println("[" + new Date() + "] " + e.toString());
            e.printStackTrace(pw);
        } catch (IOException ioe) {
            System.err.println("Error writing to log file: " + ioe.getMessage());
        }
    }
}
