package controller;

import exception.InvalidInputException;

public class InputValidator {

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

    public static String validateName(String name) throws InvalidInputException {
        name = name.trim();
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            throw new InvalidInputException("Name must contain only letters and spaces.");
        }
        return name;
    }

    public static String validateEquipmentName(String equipmentName) throws InvalidInputException {
        equipmentName = equipmentName.trim();
        if (equipmentName.isEmpty()) {
            throw new InvalidInputException("Equipment name cannot be empty.");
        } else if (!equipmentName.matches("^[a-zA-Z0-9\\s]+$")) {
            throw new InvalidInputException("Equipment name can only contain letters, numbers, and spaces.");
        }
        return equipmentName;
    }


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
