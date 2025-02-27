package controller;

import java.util.Scanner;

public class InputValidator {

    // === User Input Validation Methods ===
    /**
     * Prompts the user until a valid integer is entered.
     * Leading/trailing whitespace is trimmed.
     *
     * @param scanner the Scanner to read input from
     * @param prompt  the prompt message to display
     * @return the valid integer entered by the user
     */
    public static int getInt(Scanner scanner, String prompt) {
        int result = 0;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                result = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                valid = true;
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Discard invalid input
            }
        }
        return result;
    }

    /**
     * Prompts the user until a non-empty string is entered.
     *
     * @param scanner the Scanner to read input from
     * @param prompt  the prompt message to display
     * @return the non-empty string input
     */
    public static String getNonEmptyString(Scanner scanner, String prompt) {
        String result;
        do {
            System.out.print(prompt);
            result = scanner.nextLine().trim();
            if (result.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
            }
        } while (result.isEmpty());
        return result;
    }

    /**
     * Prompts the user for a valid email based on the role.
     * For "Student" role, the local part must match the pattern "^C00\\d+".
     * For other roles, only letters are allowed before "@setu.ie".
     *
     * @param scanner the Scanner to read input from
     * @param role    the role that influences the email pattern
     * @return a valid email address ending with "@setu.ie"
     */
    public static String getValidatedEmail(Scanner scanner, String role) {
        String email = "";
        boolean valid = false;
        while (!valid) {
            System.out.print("Email: ");
            email = scanner.nextLine().trim().toLowerCase();
            if (!email.endsWith("@setu.ie")) {
                System.out.println("Email must end with '@setu.ie'.");
                continue;
            }
            String localPart = email.substring(0, email.indexOf("@"));
            if (role.equalsIgnoreCase("Student")) {
                if (!localPart.matches("^C00\\d+$")) {
                    System.out.println("Student email must start with 'C00' followed by numbers.");
                    continue;
                }
            } else {
                if (!localPart.matches("^[a-zA-Z]+\\.[a-zA-Z]+$")) {
                    System.out.println("Email for this role must 'firstname.lastname' before '@setu.ie'.");
                    continue;
                }
            }
            valid = true;
        }
        return email;
    }

    /**
     * Prompts the user until a valid name is entered.
     * A valid name consists of only letters and spaces.
     *
     * @param scanner the Scanner to read input from
     * @param prompt  the prompt message to display
     * @return a valid name
     */
    public static String getValidatedName(Scanner scanner, String prompt) {
        String name = "";
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            name = scanner.nextLine().trim();
            if (name.matches("^[a-zA-Z\\s]+$")) {
                valid = true;
            } else {
                System.out.println("Invalid name. Name must contain only letters and spaces.");
            }
        }
        return name;
    }

    /**
     * Checks if the provided email is valid based on role-specific rules.
     * For a valid email, it must end with "@setu.ie". For "Student" role,
     * the local part must match "^C00\\d+$"; for others, it must match "^[a-zA-Z]+$".
     *
     * @param email the email to validate
     * @param role  the role which determines the email format
     * @return true if the email is valid; false otherwise
     */
    public static boolean isValidEmail(String email, String role) {
        if (!email.endsWith("@setu.ie")) {
            return false;
        }
        String localPart = email.substring(0, email.indexOf("@"));
        if (role.equalsIgnoreCase("Student")) {
            return localPart.matches("^C00\\d+$");
        } else {
            return localPart.matches("^[a-zA-Z]+$");
        }
    }

    /**
     * Checks if the provided name is valid.
     * A valid name contains only letters and spaces.
     *
     * @param name the name to validate
     * @return true if the name is valid; false otherwise
     */
    public static boolean isValidName(String name) {
        return name.matches("^[a-zA-Z\\s]+$");
    }

    // === Equipment Input Validation Methods ===

    /**
     * Prompts the user until a valid equipment name is entered.
     * Equipment name must be non-empty and can contain letters, numbers, and spaces.
     *
     * @param scanner the Scanner to read input from
     * @param prompt  the prompt message to display
     * @return a valid equipment name
     */
    public static String getValidatedEquipmentName(Scanner scanner, String prompt) {
        String name;
        while (true) {
            System.out.print(prompt);
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Equipment name cannot be empty.");
            } else if (!name.matches("^[a-zA-Z0-9\\s]+$")) {
                System.out.println("Equipment name can only contain letters, numbers, and spaces.");
            } else {
                return name;
            }
        }
    }

    /**
     * Prompts the user until a valid equipment condition is entered.
     * Valid conditions are "New", "Good", "Fair", or "Poor" (case-insensitive).
     *
     * @param scanner the Scanner to read input from
     * @param prompt  the prompt message to display
     * @return a valid equipment condition
     */
    public static String getValidatedEquipmentCondition(Scanner scanner, String prompt) {
        String condition;
        while (true) {
            System.out.print(prompt);
            condition = scanner.nextLine().trim();
            if (condition.equalsIgnoreCase("New") ||
                    condition.equalsIgnoreCase("Good") ||
                    condition.equalsIgnoreCase("Fair") ||
                    condition.equalsIgnoreCase("Poor")) {
                return condition;
            } else {
                System.out.println("Invalid condition. Please enter one of: New, Good, Fair, Poor.");
            }
        }
    }

    /**
     * Prompts the user until a valid equipment description is entered.
     * The description must be non-empty and can only contain letters, numbers, and spaces.
     *
     * @param scanner the Scanner to read input from
     * @param prompt  the prompt message to display
     * @return a valid equipment description
     */
    public static String getValidatedEquipmentDescription(Scanner scanner, String prompt) {
        String description;
        while (true) {
            System.out.print(prompt);
            description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                System.out.println("Equipment description cannot be empty.");
            } else if (!description.matches("^[a-zA-Z0-9\\s]+$")) {
                System.out.println("Equipment description can only contain letters, numbers, and spaces.");
            } else {
                return description;
            }
        }
    }

}
