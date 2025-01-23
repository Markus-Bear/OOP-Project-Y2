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
}
