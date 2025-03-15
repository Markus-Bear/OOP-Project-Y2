package controller;

import exception.InvalidInputException;

/**
 * Utility class for validating user input.
 *
 * This class provides methods to validate email addresses, names,
 * equipment names, and equipment descriptions. If the input is invalid,
 * the methods throw an {@link InvalidInputException} with a descriptive message.
 * The class does not perform any logging or error handling beyond throwing the exceptions.
 * It is the responsibility of the caller (e.g. a controller) to catch these exceptions,
 * log the error details if necessary, and display a generic error message to the user.
 */
public class InputValidator {

    /**
     * Validates an email address based on the provided role.
     *
     * For a Student, the local part must match the pattern {@code ^C00\\d+$}.
     * For non-students, the local part must match the pattern {@code ^[a-zA-Z]+\\.[a-zA-Z]+$}.
     * In all cases, the email must end with {@code @setu.ie}.
     *
     *
     * @param email the email address to validate.
     * @param role  the role of the user (e.g., "Student", "Lecturer").
     * @return the validated email address.
     * @throws InvalidInputException if the email does not match the required format.
     */
    public static String validateEmail(String email, String role) throws InvalidInputException {
        email = email.trim();
        if (!email.toLowerCase().endsWith("@setu.ie")) {
            throw new InvalidInputException("Email must end with '@setu.ie'.");
        }
        int atIndex = email.indexOf("@");
        String localPart = email.substring(0, atIndex);
        if (role.equalsIgnoreCase("Student")) {
            if (!localPart.matches("^C00\\d+$")) {
                throw new InvalidInputException("Student email must start with 'C00' followed by numbers.");
            }
        } else {
            email = email.toLowerCase();
            localPart = email.substring(0, email.indexOf("@"));
            if (!localPart.matches("^[a-zA-Z]+\\.[a-zA-Z]+$")) {
                throw new InvalidInputException("Email for this role must be in the format 'firstname.lastname' before '@setu.ie'.");
            }
        }
        return email;
    }

    /**
     * Validates a name ensuring that it contains only letters and spaces.
     *
     * @param name the name to validate.
     * @return the validated name.
     * @throws InvalidInputException if the name contains invalid characters.
     */
    public static String validateName(String name) throws InvalidInputException {
        name = name.trim();
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            throw new InvalidInputException("Name must contain only letters and spaces.");
        }
        return name;
    }

    /**
     * Validates an equipment name.
     *
     * The equipment name cannot be empty and can only contain letters, numbers, and spaces.
     *
     * @param equipmentName the equipment name to validate.
     * @return the validated equipment name.
     * @throws InvalidInputException if the equipment name is empty or contains invalid characters.
     */
    public static String validateEquipmentName(String equipmentName) throws InvalidInputException {
        equipmentName = equipmentName.trim();
        if (equipmentName.isEmpty()) {
            throw new InvalidInputException("Equipment name cannot be empty.");
        } else if (!equipmentName.matches("^[a-zA-Z0-9\\s]+$")) {
            throw new InvalidInputException("Equipment name can only contain letters, numbers, and spaces.");
        }
        return equipmentName;
    }

    /**
     * Validates an equipment description.
     *
     * The equipment description cannot be empty and can only contain letters, numbers, and spaces.
     *
     * @param description the equipment description to validate.
     * @return the validated equipment description.
     * @throws InvalidInputException if the description is empty or contains invalid characters.
     */
    public static String validateEquipmentDescription(String description) throws InvalidInputException {
        description = description.trim();
        if (description.isEmpty()) {
            throw new InvalidInputException("Equipment description cannot be empty.");
        } else if (!description.matches("^[a-zA-Z0-9\\s]+$")) {
            throw new InvalidInputException("Equipment description can only contain letters, numbers, and spaces.");
        }
        return description;
    }
}
