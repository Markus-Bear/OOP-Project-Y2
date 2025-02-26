package controller;

import model.User;
import java.util.Scanner;
import exception.AuthenticationException;

public class LoginManager {
    private final UserController userController = new UserController();

    public User loginUser() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter your email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Enter your password: ");
            String password = scanner.nextLine().trim();

            if (email.isEmpty() || password.isEmpty()) {
                throw new AuthenticationException("Email or password cannot be empty.");
            }

            User user = userController.login(email, password);
            if (user == null) {
                throw new AuthenticationException("Invalid credentials. Please try again.");
            }

            System.out.println("\nLogin successful! Welcome, " + user.getName() + ".");
            return user;
        } catch (AuthenticationException e) {
            System.err.println("Login Error: " + e.getMessage());
            return null;
        } catch (Exception e) {
            // Catch any unexpected exceptions that may occur
            System.err.println("An unexpected error occurred during login: " + e.getMessage());
            return null;
        }
    }
}
