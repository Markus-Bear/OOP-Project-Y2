package controller;

import model.Equipment;
import model.User;

import java.util.List;
import java.util.Scanner;

public class MethodsUtil {

    private static final UserController userController = new UserController();
    private static final EquipmentController equipmentController = new EquipmentController();

    // Display all users
    public void displayAllUsers() {
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
    }
    //Add a new user to the database
    public void addNewUser(Scanner scanner) {
        System.out.println("\n=== Add New User ===");

        User newUser = new User();
        System.out.print("Email: ");
        newUser.setEmail(scanner.nextLine());

        System.out.print("Password: ");
        newUser.setPassword(scanner.nextLine());

        System.out.print("Name: ");
        newUser.setName(scanner.nextLine());

        System.out.print("Role (Student/Lecturer/Admin/MediaStaff): ");
        String role = scanner.nextLine().trim();
        newUser.setRole(role);

        // Initialize all role-specific fields to null
        newUser.setCourse(null);
        newUser.setDepartment(null);
        newUser.setYear(null);

        if (role.equalsIgnoreCase("Student")) {
            System.out.print("Course: ");
            newUser.setCourse(scanner.nextLine());
            System.out.print("Department: ");
            newUser.setDepartment(promptForDepartment(scanner));
            System.out.print("Year: ");
            newUser.setYear(scanner.nextInt());
            scanner.nextLine(); // Consume newline
        } else if (role.equalsIgnoreCase("Lecturer")) {
            System.out.print("Department: ");
            newUser.setDepartment(promptForDepartment(scanner));
        }

        System.out.print("Admin ID (for verification): ");
        String adminId = scanner.nextLine();

        if (userController.addUser(newUser, adminId)) {
            System.out.println("User added successfully!");
        } else {
            System.out.println("Failed to add user. Check logs for details.");
        }
    }

    //Update a users information
    public void updateUser(Scanner scanner) {
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

        if ("Student".equals(currentUser.getRole())) {
            System.out.printf("%-15s: %s\n", "Course", currentUser.getCourse());
            System.out.printf("%-15s: %s\n", "Department", currentUser.getDepartment());
            System.out.printf("%-15s: %s\n", "Year", currentUser.getYear());
        } else if ("Lecturer".equals(currentUser.getRole())) {
            System.out.printf("%-15s: %s\n", "Department", currentUser.getDepartment());
        }

        // Collect updates
        User updatedUser = new User();
        updatedUser.setUserId(userId);

        // Email update
        System.out.print("\nCurrent email [" + currentUser.getEmail() + "]. New email: ");
        String input = scanner.nextLine();
        if (input.isEmpty()) {
            updatedUser.setEmail(currentUser.getEmail());
        } else {
            updatedUser.setEmail(input);
        }

        // Name update
        System.out.print("Current name [" + currentUser.getName() + "]. New name: ");
        input = scanner.nextLine();
        if (input.isEmpty()) {
            updatedUser.setName(currentUser.getName());
        } else {
            updatedUser.setName(input);
        }

        // Role
        updatedUser.setRole(currentUser.getRole());

        // Handle role-specific fields
        if ("Student".equals(updatedUser.getRole())) {
            // Course update
            System.out.print("Current course [" + currentUser.getCourse() + "]. New course: ");
            input = scanner.nextLine();
            if (input.isEmpty()) {
                updatedUser.setCourse(currentUser.getCourse());
            } else {
                updatedUser.setCourse(input);
            }

            // Department update
            System.out.print("Cuurent department [" + currentUser.getDepartment() + "]. New department: ");
            String newDept = promptForDepartment(scanner, currentUser.getDepartment());
            updatedUser.setDepartment(newDept);

            // Year update
            System.out.print("Current year [" + currentUser.getYear() + "]. New year: ");
            input = scanner.nextLine();
            if (input.isEmpty()) {
                updatedUser.setYear(currentUser.getYear());
            } else {
                updatedUser.setYear(Integer.parseInt(input));
            }
        } else if ("Lecturer".equals(updatedUser.getRole())) {
            // Department update
            System.out.print("Current department [" + currentUser.getDepartment() + "]. New department: ");
            String newDept = promptForDepartment(scanner, currentUser.getDepartment());
            updatedUser.setDepartment(newDept);
        } else {
            updatedUser.setCourse(null);
            updatedUser.setDepartment(null);
            updatedUser.setYear(null);
        }

        System.out.print("Admin ID for verification: ");
        String adminId = scanner.nextLine();

        if (userController.updateUser(updatedUser, adminId)) {
            System.out.println("User updated successfully!");
        } else {
            System.out.println("Failed to update user. Check admin permissions or user existence.");
        }
    }

    // Display all equipment
    public void displayAllEquipment(){
        System.out.println("\n========== All Equipment ==========");
        List<Equipment> equipmentList = equipmentController.getAllEquipment("Admin");

        if (equipmentList.isEmpty()) {
            System.out.println("No equipment found.");
            return;
        }

        System.out.printf("%-10s %-25s %-15s %-30s %-15s %-10s\n", "Equipment ID", "Name", "Type", "Description", "Status", "State");
        System.out.println("---------------------------------------------------------------------------------------------------------");

        for (Equipment equipment : equipmentList) {
            System.out.printf("%-10s %-25s %-15s %-30s %-15s %-10s\n",
                    equipment.getEquipmentId(), equipment.getName(), equipment.getType(),
                    equipment.getDescription(), equipment.getStatus(), equipment.getState());
        }
    }

    public String promptForDepartment(Scanner scanner) {
        String[] departments = {
                "Sport Management",
                "Architecture",
                "Arts & Social Studies",
                "Built Environment",
                "Business",
                "Computing",
                "Engineering",
                "Hospitality, Tourism & Culinary Arts",
                "Early Years Education",
                "Media",
                "Design & Music",
                "Nursing",
                "Health & Psychology",
                "Science",
                "Law",
                "Sport Science"
        };

        int choice = -1; // Initialize choice

        while (choice < 1 || choice > departments.length) {
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
        }

        return departments[choice - 1]; // Return the selected department
    }

    // An overloaded method for department selection with current value
    public String promptForDepartment(Scanner scanner, String currentDepartment) {
        String[] departments = {"Sport Management", "Architecture", "Arts & Social Studies", "Built Environment", "Business",
                "Computing", "Engineering", "Hospitality, Tourism & Culinary Arts", "Early Years Education", "Media", "Design & Music",
                "Nursing", "Health & Psychology", "Science", "Law", "Sport Science"};

        System.out.println("Current Department: " + currentDepartment);
        System.out.println("Select a new department or press Enter to keep current:");

        for (int i = 0; i < departments.length; i++) {
            System.out.printf("%d. %s\n", i+1, departments[i]);
        }

        while (true) {
            System.out.print("Choice [1-"+departments.length+"]: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) return currentDepartment;

            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= departments.length) {
                    return departments[choice-1];
                }
                System.out.println("Invalid number");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }
        }
    }
}
