package controller;

import model.Equipment;
import model.User;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MethodsUtil {

    private static final UserController userController = new UserController();
    private static final EquipmentController equipmentController = new EquipmentController();

    // Display all users
    public void displayAllUsers() {
        try {
            System.out.println("\n========== All Users ==========");
            List<User> users = userController.getAllUsers("Admin");

            if (users.isEmpty()) {
                System.out.println("No users found.");
                return;
            }

            // Header with only 4 columns
            System.out.printf("%-10s %-25s %-25s %-15s\n",
                    "User ID", "Email", "Name", "Role");
            System.out.println("------------------------------------------------------------");

            // Simplified data display
            for (User user : users) {
                System.out.printf("%-10s %-25s %-25s %-15s\n",
                        user.getUserId(),
                        user.getEmail(),
                        user.getName(),
                        user.getRole());
            }
        } catch (Exception e) {
            System.out.println("An error occurred while displaying users: " + e.getMessage());
        }
    }

    // Add a new user to the database
    public void addNewUser(Scanner scanner) {
        try {
            System.out.println("\n=== Add New User ===");
            User newUser = new User();

            // Get role and set it
            String role = promptForRole(scanner);
            newUser.setRole(role);

            // Initialize role-specific fields
            newUser.setCourse(null);
            newUser.setDepartment(null);
            newUser.setYear(null);

            // Get email with validation (must end with "@setu.ie")
            newUser.setEmail(promptForEmail(scanner, role));

            // Get password (non-empty input)
            newUser.setPassword(promptForNonEmptyInput(scanner, "Password"));

            // Get name with validation (letters and spaces only)
            newUser.setName(promptForValidName(scanner));

            // Role-specific fields
            if (role.equalsIgnoreCase("Student")) {
                newUser.setCourse(promptForNonEmptyInput(scanner, "Course"));
                newUser.setDepartment(promptForDepartment(scanner));
                newUser.setYear(promptForValidYear(scanner)); // Valid year between 1 and 4
            } else if (role.equalsIgnoreCase("Lecturer")) {
                newUser.setDepartment(promptForDepartment(scanner));
            }

            // Admin verification
            String adminId = promptForNonEmptyInput(scanner, "Admin ID (for verification)");

            // Add user to system
            if (userController.addUser(newUser, adminId)) {
                System.out.println("User added successfully!");
            } else {
                System.out.println("Failed to add user. Check logs for details.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while adding a new user: " + e.getMessage());
        }
    }

    // Update a user's information
    public void updateUser(Scanner scanner) {
        try {
            System.out.println("\n=== Update User ===");

            // Get user to update
            System.out.print("Enter User ID to update: ");
            String userId = scanner.nextLine();

            User currentUser = userController.getUserById(userId);
            if (currentUser == null) {
                System.out.println("User not found.");
                return;
            }

            // Display current info (excluding password)
            System.out.println("\nCurrent Information:");
            System.out.printf("%-15s: %s\n", "User ID", currentUser.getUserId());
            System.out.printf("%-15s: %s\n", "Email", currentUser.getEmail());
            System.out.printf("%-15s: %s\n", "Name", currentUser.getName());
            System.out.printf("%-15s: %s\n", "Role", currentUser.getRole());

            if ("Student".equalsIgnoreCase(currentUser.getRole())) {
                System.out.printf("%-15s: %s\n", "Course", currentUser.getCourse());
                System.out.printf("%-15s: %s\n", "Department", currentUser.getDepartment());
                System.out.printf("%-15s: %s\n", "Year", currentUser.getYear());
            } else if ("Lecturer".equalsIgnoreCase(currentUser.getRole())) {
                System.out.printf("%-15s: %s\n", "Department", currentUser.getDepartment());
            }

            // Prepare updated user object
            User updatedUser = new User();
            updatedUser.setUserId(userId);

            // ----- Email update -----
            System.out.print("\nCurrent email [" + currentUser.getEmail() + "]. New email (press Enter to keep current): ");
            String emailInput = scanner.nextLine().trim();
            if (emailInput.isEmpty()) {
                updatedUser.setEmail(currentUser.getEmail());
            } else {
                // Validate new email using the same rules as in addNewUser
                while (!isValidEmail(emailInput, currentUser.getRole())) {
                    System.out.print("Invalid email. Please enter a valid email (or press Enter to keep current): ");
                    emailInput = scanner.nextLine().trim();
                    if (emailInput.isEmpty()) {
                        emailInput = currentUser.getEmail();
                        break;
                    }
                }
                updatedUser.setEmail(emailInput);
            }

            // ----- Name update -----
            System.out.print("Current name [" + currentUser.getName() + "]. New name (press Enter to keep current): ");
            String nameInput = scanner.nextLine().trim();
            if (nameInput.isEmpty()) {
                updatedUser.setName(currentUser.getName());
            } else {
                while (!isValidName(nameInput)) {
                    System.out.print("Invalid name. Name must contain only letters and spaces. Enter a valid name (or press Enter to keep current): ");
                    nameInput = scanner.nextLine().trim();
                    if (nameInput.isEmpty()) {
                        nameInput = currentUser.getName();
                        break;
                    }
                }
                updatedUser.setName(nameInput);
            }

            // Role remains unchanged
            updatedUser.setRole(currentUser.getRole());

            // ----- Role-specific fields -----
            if ("Student".equalsIgnoreCase(updatedUser.getRole())) {
                // Course update (non-empty string)
                System.out.print("Current course [" + currentUser.getCourse() + "]. New course (press Enter to keep current): ");
                String courseInput = scanner.nextLine().trim();
                if (courseInput.isEmpty()) {
                    updatedUser.setCourse(currentUser.getCourse());
                } else {
                    // For course, we simply ensure that the input is non-empty (already true)
                    updatedUser.setCourse(courseInput);
                }

                // Department update using the overloaded method
                System.out.print("Current department [" + currentUser.getDepartment() + "]. ");
                String newDept = promptForDepartment(scanner, currentUser.getDepartment());
                updatedUser.setDepartment(newDept);

                // Year update (1-4)
                System.out.print("Current year [" + currentUser.getYear() + "]. New year (press Enter to keep current): ");
                String yearInput = scanner.nextLine().trim();
                if (yearInput.isEmpty()) {
                    updatedUser.setYear(currentUser.getYear());
                } else {
                    boolean validYear = false;
                    int newYear = currentUser.getYear();
                    while (!validYear) {
                        try {
                            newYear = Integer.parseInt(yearInput);
                            if (newYear >= 1 && newYear <= 4) {
                                validYear = true;
                            } else {
                                System.out.print("Year must be between 1 and 4. Enter a valid year (or press Enter to keep current): ");
                                yearInput = scanner.nextLine().trim();
                                if (yearInput.isEmpty()) {
                                    newYear = currentUser.getYear();
                                    validYear = true;
                                }
                            }
                        } catch (NumberFormatException e) {
                            System.out.print("Invalid number format. Enter a valid year (1-4) (or press Enter to keep current): ");
                            yearInput = scanner.nextLine().trim();
                            if (yearInput.isEmpty()) {
                                newYear = currentUser.getYear();
                                validYear = true;
                            }
                        }
                    }
                    updatedUser.setYear(newYear);
                }
            } else if ("Lecturer".equalsIgnoreCase(updatedUser.getRole())) {
                // Department update using the overloaded helper method
                System.out.print("Current department [" + currentUser.getDepartment() + "]. ");
                String newDept = promptForDepartment(scanner, currentUser.getDepartment());
                updatedUser.setDepartment(newDept);
            } else {
                // For other roles, clear role-specific fields
                updatedUser.setCourse(null);
                updatedUser.setDepartment(null);
                updatedUser.setYear(null);
            }

            // ----- Admin verification -----
            System.out.print("Admin ID for verification: ");
            String adminId = scanner.nextLine();

            if (userController.updateUser(updatedUser, adminId)) {
                System.out.println("User updated successfully!");
            } else {
                System.out.println("Failed to update user. Check admin permissions or user existence.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while updating the user: " + e.getMessage());
        }
    }

    // Helper method: Validate email based on role
    private boolean isValidEmail(String email, String role) {
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

    // Helper method: Validate name (only letters and spaces allowed)
    private boolean isValidName(String name) {
        return name.matches("^[a-zA-Z\\s]+$");
    }

    // Delete a user's information
    public void deleteUser(Scanner scanner) {
        try {
            System.out.println("\n=== Delete User ===");
            System.out.print("Enter User ID to delete: ");
            String userId = scanner.nextLine();
            System.out.print("Enter Admin ID for verification: ");
            String adminId = scanner.nextLine();

            if (userController.deleteUser(userId, adminId)) {
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("Failed to delete user. Check admin permissions or user existence.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while deleting the user: " + e.getMessage());
        }
    }

    // Display all equipment
    public void displayAllEquipment() {
        try {
            System.out.println("\n========== All Equipment ==========");
            List<Equipment> equipmentList = equipmentController.getAllEquipment("Admin");

            if (equipmentList.isEmpty()) {
                System.out.println("No equipment found.");
                return;
            }

            System.out.printf("%-10s %-25s %-15s %-30s %-15s %-10s\n",
                    "Equipment ID", "Name", "Type", "Description", "Status", "State");
            System.out.println("---------------------------------------------------------------------------------------------------------");

            for (Equipment equipment : equipmentList) {
                System.out.printf("%-10s %-25s %-15s %-30s %-15s %-10s\n",
                        equipment.getEquipmentId(), equipment.getName(), equipment.getType(),
                        equipment.getDescription(), equipment.getStatus(), equipment.getState());
            }
        } catch (Exception e) {
            System.out.println("An error occurred while displaying equipment: " + e.getMessage());
        }
    }

    // Display equipment based on type selected
    public void viewEquipmentByType(Scanner scanner) {
        try {
            System.out.println("\n=== View Equipment by Type ===");

            // List of equipment types
            String[] equipmentTypes = {
                    "Audio Recorder",
                    "Camera",
                    "Drone",
                    "Laptop",
                    "Lighting",
                    "Projector",
                    "VR Headset"
            };

            // Display types in a numbered list
            System.out.println("Select a type:");
            for (int i = 0; i < equipmentTypes.length; i++) {
                System.out.printf("%d. %s\n", i + 1, equipmentTypes[i]);
            }

            // Prompt user to select a type
            System.out.print("Enter your choice (1-" + equipmentTypes.length + "): ");
            int choice = 0;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException ime) {
                System.out.println("Invalid input type. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
                return;
            }
            scanner.nextLine(); // Consume newline

            // Validate choice
            if (choice < 1 || choice > equipmentTypes.length) {
                System.out.println("Invalid choice. Please try again.");
                return;
            }

            // Get the selected type
            String selectedType = equipmentTypes[choice - 1];

            // Fetch and display equipment of the selected type
            List<Equipment> equipmentList = equipmentController.getEquipmentByType(selectedType, "Admin"); // Assuming "Admin" role for simplicity

            if (equipmentList.isEmpty()) {
                System.out.println("No equipment found for type: " + selectedType);
            } else {
                System.out.println("\n========== Equipment of Type: " + selectedType + " ==========");
                System.out.printf("%-10s %-25s %-15s %-30s %-15s %-10s\n",
                        "Equipment ID", "Name", "Type", "Description", "Status", "State");
                System.out.println("---------------------------------------------------------------------------------------------------------");

                for (Equipment equipment : equipmentList) {
                    System.out.printf("%-10s %-25s %-15s %-30s %-15s %-10s\n",
                            equipment.getEquipmentId(), equipment.getName(), equipment.getType(),
                            equipment.getDescription(), equipment.getStatus(), equipment.getState());
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred while viewing equipment by type: " + e.getMessage());
        }
    }

    // Prompts user to select a role
    public String promptForRole(Scanner scanner) {
        String[] roles = { "Student", "Lecturer", "Admin", "MediaStaff" }; // Predefined roles
        int choice = -1; // Initialize choice

        while (choice < 1 || choice > roles.length) {
            try {
                System.out.println("Select a role:");
                for (int i = 0; i < roles.length; i++) {
                    System.out.printf("%d. %s\n", i + 1, roles[i]); // Display available roles with numbers
                }
                System.out.print("Enter your choice (1-" + roles.length + "): ");

                if (scanner.hasNextInt()) { // Check if the input is an integer
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Clear the newline character
                    if (choice < 1 || choice > roles.length) {
                        System.out.println("Invalid choice. Please select a number between 1 and " + roles.length + ".");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number between 1 and " + roles.length + ".");
                    scanner.nextLine(); // Clear invalid input from the buffer
                }
            } catch (Exception e) {
                System.out.println("Error selecting role: " + e.getMessage());
                scanner.nextLine(); // Clear any residual input
            }
        }

        return roles[choice - 1]; // Return the selected role
    }

    // Prompts user for an email with validation based on role
    private String promptForEmail(Scanner scanner, String role) {
        while (true) {
            try {
                System.out.print("Email: ");
                String email = scanner.nextLine().trim();

                // Check if email ends with "@setu.ie"
                if (!email.endsWith("@setu.ie")) {
                    System.out.println("Email must end with '@setu.ie'.");
                } else {
                    // Extract the local part (before the '@')
                    String localPart = email.substring(0, email.indexOf("@"));

                    if (role.equalsIgnoreCase("Student")) {
                        // For Students, validate that the local part starts with "C00" followed by numbers
                        if (!localPart.matches("^C00\\d+$")) {
                            System.out.println("Student email must start with 'C00' followed by numbers.");
                        } else {
                            return email;
                        }
                    } else {
                        // For other roles, only letters are allowed before "@setu.ie"
                        if (!localPart.matches("^[a-zA-Z]+$")) {
                            System.out.println("Email for this role must contain only letters before '@setu.ie'.");
                        } else {
                            return email;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error reading email: " + e.getMessage());
            }
        }
    }

    // Prompts for non-empty input for a given field name
    private String promptForNonEmptyInput(Scanner scanner, String fieldName) {
        while (true) {
            try {
                System.out.print(fieldName + ": ");
                String input = scanner.nextLine().trim();
                if (!input.isEmpty()) {
                    return input;
                }
                System.out.println("Error: " + fieldName + " cannot be empty.");
            } catch (Exception e) {
                System.out.println("Error reading input for " + fieldName + ": " + e.getMessage());
            }
        }
    }

    // Prompts for a valid name (letters and spaces only)
    private String promptForValidName(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Name: ");
                String name = scanner.nextLine().trim();
                if (name.matches("^[a-zA-Z\\s]+$")) { // Allow only letters and spaces
                    return name;
                }
                System.out.println("Invalid name: Name must contain only letters and spaces.");
            } catch (Exception e) {
                System.out.println("Error reading name: " + e.getMessage());
            }
        }
    }

    // Prompts for a valid year (between 1 and 4)
    private int promptForValidYear(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Year (1-4): ");
                String input = scanner.nextLine().trim();
                int year = Integer.parseInt(input);
                if (year >= 1 && year <= 4) {
                    return year;
                } else {
                    System.out.println("Year must be between 1 and 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number for year.");
            } catch (Exception e) {
                System.out.println("Error reading year: " + e.getMessage());
            }
        }
    }

    // Prompts the user to select a department
    public String promptForDepartment(Scanner scanner) {
        String[] departments = {
                "Sport Management", "Architecture", "Arts & Social Studies", "Built Environment", "Business",
                "Computing", "Engineering", "Hospitality, Tourism & Culinary Arts", "Early Years Education", "Media", "Design & Music",
                "Nursing", "Health & Psychology", "Science", "Law", "Sport Science"
        };

        int choice = -1; // Initialize choice

        while (choice < 1 || choice > departments.length) {
            try {
                System.out.println("Select a department:");
                for (int i = 0; i < departments.length; i++) {
                    System.out.printf("%d. %s\n", i + 1, departments[i]);
                }

                System.out.print("Enter your choice (1-" + departments.length + "): ");

                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Clear the newline character
                    if (choice < 1 || choice > departments.length) {
                        System.out.println("Invalid choice. Please select a number between 1 and " + departments.length + ".");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number between 1 and " + departments.length + ".");
                    scanner.nextLine(); // Clear invalid input from the buffer
                }
            } catch (Exception e) {
                System.out.println("Error selecting department: " + e.getMessage());
                scanner.nextLine(); // Clear any residual input
            }
        }

        return departments[choice - 1]; // Return the selected department
    }

    // Overloaded method for department selection with a current value provided
    public String promptForDepartment(Scanner scanner, String currentDepartment) {
        String[] departments = {
                "Sport Management", "Architecture", "Arts & Social Studies", "Built Environment", "Business",
                "Computing", "Engineering", "Hospitality, Tourism & Culinary Arts", "Early Years Education", "Media", "Design & Music",
                "Nursing", "Health & Psychology", "Science", "Law", "Sport Science"
        };

        System.out.println("Current Department: " + currentDepartment);
        System.out.println("Select a new department or press Enter to keep current:");

        for (int i = 0; i < departments.length; i++) {
            System.out.printf("%d. %s\n", i + 1, departments[i]);
        }

        while (true) {
            try {
                System.out.print("Choice [1-" + departments.length + "]: ");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    return currentDepartment;
                }

                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= departments.length) {
                    return departments[choice - 1];
                }
                System.out.println("Invalid number");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            } catch (Exception e) {
                System.out.println("Error selecting department: " + e.getMessage());
            }
        }
    }

}
