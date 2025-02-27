package controller;

import model.User;
import java.util.Scanner;
import exception.AuthenticationException;

/**
 * Handles the user login process.
 * <p>
 * Note: Password verification is handled in UserDAO (using PasswordUtils)
 */
public class LoginManager {
    private final UserController userController = new UserController();

    /**
     * Prompts the user for their email and password and attempts to authenticate.
     * Allows up to 3 login attempts. If authentication fails after 3 attempts,
     * the program terminates.
     *
     * @return the authenticated User if login is successful; otherwise, the program exits.
     */
    public User loginUser() {
        Scanner scanner = new Scanner(System.in);
        int attempts = 0;

        while (attempts < 3) {
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
                attempts++;
                System.err.println("Login Error: " + e.getMessage());
                if (attempts < 3) {
                    System.out.println("Please try again. Attempts remaining: " + (3 - attempts));
                }
            } catch (Exception e) {
                System.err.println("An unexpected error occurred during login: " + e.getMessage());
                return null;
            }
        }

        System.out.println("\n\nToo many failed login attempts. Terminating program.");
        System.exit(0);
        return null; // This line will never be reached. Well it shouldn't.
    }

}
