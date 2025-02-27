package controller;

import model.User;
import model.Equipment;
import model.Reservation;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for handling various methods for user, equipment, and reservation management.
 */
public class MethodsUtil {

    private static final UserController userController = new UserController();
    private static final EquipmentController equipmentController = new EquipmentController();
    private final ReservationController reservationController = new ReservationController();
    private final CheckoutController checkoutController = new CheckoutController();

    // ====================================================
    // ============== User-Related Methods ================
    // ====================================================

    /**
     * Displays the profile details of the logged-in user.
     *
     * @param loggedInUser the currently logged-in user.
     */
    public void viewProfile(User loggedInUser) {
        System.out.println("\n===== Your Profile =====");
        System.out.printf("User ID        : %s\n", loggedInUser.getUserId());
        System.out.printf("Name           : %s\n", loggedInUser.getName());
        System.out.printf("Email          : %s\n", loggedInUser.getEmail());
        System.out.printf("Role           : %s\n", loggedInUser.getRole());

        // Display department for both students and lecturers
        if ("Student".equalsIgnoreCase(loggedInUser.getRole()) ||
                "Lecturer".equalsIgnoreCase(loggedInUser.getRole())) {
            System.out.printf("Department     : %s\n",
                    loggedInUser.getDepartment() != null ? loggedInUser.getDepartment() : "N/A");
        }

        // Display course & year ONLY for students
        if ("Student".equalsIgnoreCase(loggedInUser.getRole())) {
            System.out.printf("Course         : %s\n",
                    loggedInUser.getCourse() != null ? loggedInUser.getCourse() : "N/A");
            System.out.printf("Year           : %s\n",
                    loggedInUser.getYear() != null ? loggedInUser.getYear() : "N/A");
        }
        System.out.println("========================");
    }

    /**
     * Displays all users with their user ID, email, name, and role.
     */
    public void displayAllUsers() {
        try {
            System.out.println("\n========== All Users ==========");
            List<User> users = userController.getAllUsers("Admin");
            if (users.isEmpty()) {
                System.out.println("No users found.");
                return;
            }
            System.out.printf("%-10s %-25s %-25s %-15s\n", "User ID", "Email", "Name", "Role");
            System.out.println("------------------------------------------------------------");
            for (User user : users) {
                System.out.printf("%-10s %-25s %-25s %-15s\n",
                        user.getUserId(), user.getEmail(), user.getName(), user.getRole());
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while displaying users: " + e.getMessage());
        }
    }

    /**
     * Displays only lecturers and students.
     */
    public void displayLecturersAndStudents() {
        try {
            System.out.println("\n===== List of Lecturers & Students =====");
            List<User> users = userController.getLecturersAndStudents();
            if (users.isEmpty()) {
                System.out.println("No lecturers or students found.");
                return;
            }
            System.out.printf("%-10s %-25s %-25s %-15s\n", "User ID", "Email", "Name", "Role");
            System.out.println("------------------------------------------------------------");
            for (User user : users) {
                System.out.printf("%-10s %-25s %-25s %-15s\n",
                        user.getUserId(), user.getEmail(), user.getName(), user.getRole());
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while displaying lecturers and students: " + e.getMessage());
        }
    }

    /**
     * Adds a new user using input validations.
     *
     * @param scanner the Scanner to read input.
     */
    public void addNewUser(Scanner scanner) {
        try {
            System.out.println("\n=== Add New User ===");
            User newUser = new User();
            String role = promptForRole(scanner);
            newUser.setRole(role);
            newUser.setCourse(null);
            newUser.setDepartment(null);
            newUser.setYear(null);

            // Use InputValidator for validations
            newUser.setEmail(InputValidator.getValidatedEmail(scanner, role));

            // Hash the password using PasswordUtils
            String plainPassword = InputValidator.getNonEmptyString(scanner, "Password: ");
            String hashedPassword = PasswordUtils.hashPassword(plainPassword);
            newUser.setPassword(hashedPassword);

            newUser.setName(InputValidator.getValidatedName(scanner, "Name: "));

            // Role-specific fields
            if (role.equalsIgnoreCase("Student")) {
                String department = promptForDepartment(scanner);
                newUser.setDepartment(department);
                newUser.setCourse(promptForCourse(scanner, department));
                newUser.setYear(promptForValidYear(scanner));
            } else if (role.equalsIgnoreCase("Lecturer")) {
                newUser.setDepartment(promptForDepartment(scanner));
            }

            String adminId = InputValidator.getNonEmptyString(scanner, "Admin ID (for verification): ");
            if (userController.addUser(newUser, adminId)) {
                System.out.println("User added successfully!");
            } else {
                System.out.println("Failed to add user. Check logs for details.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while adding a new user: " + e.getMessage());
        }
    }



    /**
     * Updates an existing user's information.
     *
     * @param scanner the Scanner to read input.
     */
    public void updateUser(Scanner scanner) {
        try {
            System.out.println("\n=== Update User ===");
            List<User> users = userController.getAllUsers("Admin");
            if (users.isEmpty()) {
                System.out.println("No users found.");
                return;
            }
            System.out.println("Select a user to update:");
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                System.out.printf("%d. %s (ID: %s)\n", i + 1, user.getName(), user.getUserId());
            }
            int choice = InputValidator.getInt(scanner, "Enter the number corresponding to the user: ");
            while (choice < 1 || choice > users.size()) {
                System.out.println("Invalid selection. Try again.");
                choice = InputValidator.getInt(scanner, "Enter the number corresponding to the user: ");
            }
            User currentUser = users.get(choice - 1);

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

            User updatedUser = new User();
            updatedUser.setUserId(currentUser.getUserId());

            System.out.print("\nCurrent email [" + currentUser.getEmail() + "]. New email (press Enter to keep current): ");
            String emailInput = scanner.nextLine().trim();
            if (emailInput.isEmpty()) {
                updatedUser.setEmail(currentUser.getEmail());
            } else {
                updatedUser.setEmail(InputValidator.getValidatedEmail(scanner, currentUser.getRole()));
            }

            System.out.print("Current name [" + currentUser.getName() + "]. New name (press Enter to keep current): ");
            String nameInput = scanner.nextLine().trim();
            if (nameInput.isEmpty()) {
                updatedUser.setName(currentUser.getName());
            } else {
                updatedUser.setName(InputValidator.getValidatedName(scanner, "New name: "));
            }

            // New password update: prompt for new password and hash it if provided
            System.out.print("Enter new password (press Enter to keep current): ");
            String newPassword = scanner.nextLine().trim();
            if (newPassword.isEmpty()) {
                updatedUser.setPassword(currentUser.getPassword());
            } else {
                String hashedPassword = PasswordUtils.hashPassword(newPassword);
                updatedUser.setPassword(hashedPassword);
            }

            updatedUser.setRole(currentUser.getRole());

            // Role-specific updates
            if ("Student".equalsIgnoreCase(updatedUser.getRole())) {
                System.out.print("Current department [" + currentUser.getDepartment() + "]. ");
                String newDept = promptForDepartment(scanner, currentUser.getDepartment());
                updatedUser.setDepartment(newDept);
                updatedUser.setCourse(promptForCourse(scanner, newDept, currentUser.getCourse()));
                System.out.print("Current year [" + currentUser.getYear() + "]. New year (press Enter to keep current): ");
                String yearInput = scanner.nextLine().trim();
                if (yearInput.isEmpty()) {
                    updatedUser.setYear(currentUser.getYear());
                } else {
                    int newYear = promptForValidYear(scanner);
                    updatedUser.setYear(newYear);
                }
            } else if ("Lecturer".equalsIgnoreCase(updatedUser.getRole())) {
                System.out.print("Current department [" + currentUser.getDepartment() + "]. ");
                String newDept = promptForDepartment(scanner, currentUser.getDepartment());
                updatedUser.setDepartment(newDept);
            } else {
                updatedUser.setCourse(null);
                updatedUser.setDepartment(null);
                updatedUser.setYear(null);
            }

            String adminId = InputValidator.getNonEmptyString(scanner, "Admin ID for verification: ");
            if (userController.updateUser(updatedUser, adminId)) {
                System.out.println("User updated successfully!");
            } else {
                System.out.println("Failed to update user. Check admin permissions or user existence.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while updating the user: " + e.getMessage());
        }
    }



    /**
     * Deletes a user after listing all users and confirming deletion.
     *
     * @param scanner the Scanner to read input.
     */
    public void deleteUser(Scanner scanner) {
        try {
            System.out.println("\n=== Delete User ===");
            List<User> users = userController.getAllUsers("Admin");
            if (users.isEmpty()) {
                System.out.println("No users available to delete.");
                return;
            }
            System.out.printf("%-5s %-15s %-25s %-15s\n", "No.", "User ID", "Name", "Role");
            System.out.println("------------------------------------------------------------");
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                System.out.printf("%-5d %-15s %-25s %-15s\n", i + 1, user.getUserId(), user.getName(), user.getRole());
            }
            int selection = InputValidator.getInt(scanner, "Enter the number corresponding to the user to delete: ");
            while (selection < 1 || selection > users.size()) {
                System.out.println("Invalid selection. Please try again.");
                selection = InputValidator.getInt(scanner, "Enter the number corresponding to the user to delete: ");
            }
            User selectedUser = users.get(selection - 1);
            System.out.print("Are you sure you want to delete user " + selectedUser.getName()
                    + " (ID: " + selectedUser.getUserId() + ")? (yes/no): ");
            String confirmation = scanner.nextLine().trim();
            if (!confirmation.equalsIgnoreCase("yes")) {
                System.out.println("Deletion cancelled.");
                return;
            }
            String adminId = InputValidator.getNonEmptyString(scanner, "Enter Admin ID for verification: ");
            if (userController.deleteUser(selectedUser.getUserId(), adminId)) {
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("Failed to delete user. Check admin permissions or user existence.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while deleting the user: " + e.getMessage());
        }
    }

    // ====================================================
    // ============ Equipment-Related Methods =============
    // ====================================================

    /**
     * Displays all equipment.
     */
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

    /**
     * Displays all available equipment.
     */
    public void displayAllAvailableEquipment() {
        try {
            System.out.println("\n========== Available Equipment ==========");
            List<Equipment> equipmentList = equipmentController.getEquipmentByStatus("Available");
            if (equipmentList.isEmpty()) {
                System.out.println("No available equipment at the moment.");
                return;
            }
            System.out.printf("%-10s %-25s %-15s %-30s %-10s\n",
                    "Equipment ID", "Name", "Type", "Description", "State");
            System.out.println("-------------------------------------------------------------------------------------");
            for (Equipment equipment : equipmentList) {
                System.out.printf("%-10s %-25s %-15s %-30s %-10s\n",
                        equipment.getEquipmentId(), equipment.getName(),
                        equipment.getType(), equipment.getDescription(), equipment.getState());
            }
        } catch (Exception e) {
            System.out.println("An error occurred while displaying available equipment: " + e.getMessage());
        }
    }

    /**
     * Displays equipment filtered by type.
     *
     * @param scanner the Scanner to read input.
     */
    public void viewEquipmentByType(Scanner scanner) {
        try {
            System.out.println("\n=== View Equipment by Type ===");
            String[] equipmentTypes = {"Audio Recorder", "Camera", "Drone", "Laptop", "Lighting", "Projector", "VR Headset"};
            System.out.println("Select a type:");
            for (int i = 0; i < equipmentTypes.length; i++) {
                System.out.printf("%d. %s\n", i + 1, equipmentTypes[i]);
            }
            System.out.print("Enter your choice (1-" + equipmentTypes.length + "): ");
            int choice = 0;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException ime) {
                System.out.println("Invalid input type. Please enter a number.");
                scanner.nextLine();
                return;
            }
            scanner.nextLine();
            if (choice < 1 || choice > equipmentTypes.length) {
                System.out.println("Invalid choice. Please try again.");
                return;
            }
            String selectedType = equipmentTypes[choice - 1];
            List<Equipment> equipmentList = equipmentController.getEquipmentByType(selectedType, "Admin");
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

    /**
     * Displays available equipment filtered by type.
     *
     * @param scanner the Scanner to read input.
     */
    public void viewAvailableEquipmentByType(Scanner scanner) {
        try {
            System.out.println("\n=== View Available Equipment by Type ===");
            String[] equipmentTypes = {"Audio Recorder", "Camera", "Drone", "Laptop", "Lighting", "Projector", "VR Headset"};
            System.out.println("Select a type:");
            for (int i = 0; i < equipmentTypes.length; i++) {
                System.out.printf("%d. %s\n", i + 1, equipmentTypes[i]);
            }
            int choice = -1;
            while (choice < 1 || choice > equipmentTypes.length) {
                System.out.print("Enter your choice (1-" + equipmentTypes.length + "): ");
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    if (choice < 1 || choice > equipmentTypes.length) {
                        System.out.println("Invalid choice. Please try again.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine();
                }
            }
            String selectedType = equipmentTypes[choice - 1];
            List<Equipment> equipmentList = equipmentController.getEquipmentByTypeAndStatus(selectedType, "Available");
            if (equipmentList.isEmpty()) {
                System.out.println("No available equipment found for type: " + selectedType);
                return;
            }
            System.out.println("\n========== Available " + selectedType + " ==========");
            System.out.printf("%-10s %-25s %-30s %-10s\n", "Equipment ID", "Name", "Description", "State");
            System.out.println("--------------------------------------------------------------------");
            for (Equipment equipment : equipmentList) {
                System.out.printf("%-10s %-25s %-30s %-10s\n",
                        equipment.getEquipmentId(), equipment.getName(), equipment.getDescription(), equipment.getState());
            }
        } catch (Exception e) {
            System.out.println("An error occurred while viewing available equipment by type: " + e.getMessage());
        }
    }

    /**
     * Adds new equipment using input validations from InputValidator.
     *
     * @param scanner the Scanner to read input.
     */
    public void addNewEquipment(Scanner scanner) {
        try {
            System.out.println("\n=== Add New Equipment ===");
            String name = InputValidator.getValidatedEquipmentName(scanner, "Enter Equipment Name: ");
            String type = promptForEquipmentType(scanner);
            String description = InputValidator.getValidatedEquipmentDescription(scanner, "Enter Equipment Description: ");
            String state = InputValidator.getValidatedEquipmentCondition(scanner, "Enter Equipment Condition (New, Good, Fair, Poor): ");
            System.out.print("Enter User ID (Admin or MediaStaff) for authorization: ");
            String userId = scanner.nextLine().trim();
            Equipment newEquipment = new Equipment();
            newEquipment.setName(name);
            newEquipment.setType(type);
            newEquipment.setDescription(description);
            newEquipment.setState(state);
            if (equipmentController.addEquipment(newEquipment, userId)) {
                System.out.println("Equipment added successfully!");
            } else {
                System.out.println("Failed to add equipment. Check permissions and try again.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while adding equipment: " + e.getMessage());
        }
    }

    /**
     * Updates an equipment's information using validations.
     *
     * @param scanner the Scanner to read input.
     */
    public void updateEquipment(Scanner scanner) {
        try {
            System.out.println("\n=== Update Equipment ===");
            List<Equipment> equipmentList = equipmentController.getAllEquipment("Admin");
            if (equipmentList.isEmpty()) {
                System.out.println("No equipment found.");
                return;
            }
            System.out.println("Select equipment to update:");
            for (int i = 0; i < equipmentList.size(); i++) {
                Equipment equipment = equipmentList.get(i);
                System.out.printf("%d. %s (ID: %s, Type: %s, Status: %s)\n",
                        i + 1, equipment.getName(), equipment.getEquipmentId(), equipment.getType(), equipment.getStatus());
            }
            int choice = -1;
            while (choice < 1 || choice > equipmentList.size()) {
                System.out.print("Enter the number corresponding to the equipment: ");
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    if (choice < 1 || choice > equipmentList.size()) {
                        System.out.println("Invalid selection. Try again.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine();
                }
            }
            Equipment selectedEquipment = equipmentList.get(choice - 1);
            System.out.println("\nSelected Equipment: " + selectedEquipment.getName() +
                    " (ID: " + selectedEquipment.getEquipmentId() + ")");

            // Update Equipment Name
            System.out.print("Current Name [" + selectedEquipment.getName() + "]. New Name (Press Enter to keep current): ");
            String nameInput = scanner.nextLine().trim();
            String name;
            if (nameInput.isEmpty()) {
                name = selectedEquipment.getName();
            } else {
                if (!nameInput.matches("^[a-zA-Z0-9\\s]+$")) {
                    name = InputValidator.getValidatedEquipmentName(scanner, "Enter a valid Equipment Name: ");
                } else {
                    name = nameInput;
                }
            }

            // Equipment Type update
            String type = promptForEquipmentType(scanner);

            // Update Equipment Description
            System.out.print("Current Description [" + selectedEquipment.getDescription() + "]. New Description (Press Enter to keep current): ");
            String descInput = scanner.nextLine().trim();
            String description;
            if (descInput.isEmpty()) {
                description = selectedEquipment.getDescription();
            } else {
                if (!descInput.matches("^[a-zA-Z0-9\\s]+$")) {
                    description = InputValidator.getValidatedEquipmentDescription(scanner, "Enter a valid Equipment Description: ");
                } else {
                    description = descInput;
                }
            }

            // Update Equipment Condition
            System.out.print("Current Condition [" + selectedEquipment.getState() + "]. New Condition (Press Enter to keep current): ");
            String condInput = scanner.nextLine().trim();
            String state;
            if (condInput.isEmpty()) {
                state = selectedEquipment.getState();
            } else {
                if (!condInput.equalsIgnoreCase("New") &&
                        !condInput.equalsIgnoreCase("Good") &&
                        !condInput.equalsIgnoreCase("Fair") &&
                        !condInput.equalsIgnoreCase("Poor")) {
                    state = InputValidator.getValidatedEquipmentCondition(scanner, "Enter valid Equipment Condition (New, Good, Fair, Poor): ");
                } else {
                    state = condInput;
                }
            }

            // Update Equipment Status
            System.out.println("\nSelect Equipment Status (Press Enter to keep current: " + selectedEquipment.getStatus() + ")");
            String[] statuses = {"Available", "Reserved", "CheckedOut"};
            for (int i = 0; i < statuses.length; i++) {
                System.out.println((i + 1) + ". " + statuses[i]);
            }
            String status = selectedEquipment.getStatus();
            int statusChoice = -1;
            while (true) {
                System.out.print("Enter the number corresponding to the equipment status (or press Enter to keep current): ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    break;
                }
                try {
                    statusChoice = Integer.parseInt(input);
                    if (statusChoice >= 1 && statusChoice <= statuses.length) {
                        status = statuses[statusChoice - 1];
                        break;
                    } else {
                        System.out.println("Invalid selection. Please try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            System.out.print("Enter User ID (Admin or MediaStaff) for authorization: ");
            String userId = scanner.nextLine().trim();

            Equipment updatedEquipment = new Equipment();
            updatedEquipment.setEquipmentId(selectedEquipment.getEquipmentId());
            updatedEquipment.setName(name);
            updatedEquipment.setType(type);
            updatedEquipment.setDescription(description);
            updatedEquipment.setState(state);
            updatedEquipment.setStatus(status);

            if (equipmentController.updateEquipment(updatedEquipment, userId)) {
                System.out.println("Equipment updated successfully!");
            } else {
                System.out.println("Failed to update equipment. Check permissions and try again.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while updating equipment: " + e.getMessage());
        }
    }

    /**
     * Deletes equipment, ensuring that equipment with status Reserved or CheckedOut cannot be deleted.
     *
     * @param scanner the Scanner to read input.
     */
    public void deleteEquipment(Scanner scanner) {
        try {
            System.out.println("\n=== Delete Equipment ===");
            List<Equipment> equipmentList = equipmentController.getAllEquipment("Admin");
            if (equipmentList.isEmpty()) {
                System.out.println("No equipment found.");
                return;
            }
            System.out.println("Select equipment to delete:");
            for (int i = 0; i < equipmentList.size(); i++) {
                Equipment equipment = equipmentList.get(i);
                System.out.printf("%d. %s (ID: %s, Type: %s, Status: %s)\n",
                        i + 1, equipment.getName(), equipment.getEquipmentId(),
                        equipment.getType(), equipment.getStatus());
            }
            int choice = -1;
            while (choice < 1 || choice > equipmentList.size()) {
                System.out.print("Enter the number corresponding to the equipment: ");
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    if (choice < 1 || choice > equipmentList.size()) {
                        System.out.println("Invalid selection. Try again.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine();
                }
            }
            Equipment selectedEquipment = equipmentList.get(choice - 1);
            if (selectedEquipment.getStatus().equalsIgnoreCase("Reserved") ||
                    selectedEquipment.getStatus().equalsIgnoreCase("CheckedOut")) {
                System.out.println("Cannot delete equipment that is Reserved or Checked Out.");
                return;
            }
            System.out.print("Are you sure you want to delete " + selectedEquipment.getName() +
                    " (ID: " + selectedEquipment.getEquipmentId() + ")? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();
            if (!confirmation.equals("yes")) {
                System.out.println("Deletion cancelled.");
                return;
            }
            String userId = InputValidator.getNonEmptyString(scanner, "Enter User ID (Admin or MediaStaff) for authorization: ");
            if (equipmentController.deleteEquipment(selectedEquipment.getEquipmentId(), userId)) {
                System.out.println("Equipment deleted successfully!");
            } else {
                System.out.println("Failed to delete equipment. Check permissions and try again.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while deleting equipment: " + e.getMessage());
        }
    }

    // ====================================================
    // ========== Reservation / Check-Out / Check-In Methods ==========
    // ====================================================

    /**
     * Requests a new equipment reservation. The date must be entered in dd-MM-yyyy format,
     * converted to yyyy-MM-dd, and must not be before the current date.
     *
     * @param scanner      the Scanner to read input.
     * @param loggedInUser the logged-in user making the reservation.
     */
    public void requestReservation(Scanner scanner, User loggedInUser) {
        System.out.println("\n=== Request Equipment Reservation ===");
        String userId = loggedInUser.getUserId();
        List<Equipment> availableEquipment = equipmentController.getEquipmentByStatus("Available");
        if (availableEquipment.isEmpty()) {
            System.out.println("No available equipment for reservation.");
            return;
        }
        System.out.println("\nAvailable Equipment:");
        for (int i = 0; i < availableEquipment.size(); i++) {
            Equipment eq = availableEquipment.get(i);
            System.out.printf("%d. %s (%s)\n", i + 1, eq.getName(), eq.getEquipmentId());
        }
        int choice = -1;
        while (choice < 1 || choice > availableEquipment.size()) {
            System.out.print("\nEnter the number of the equipment you want to reserve: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice < 1 || choice > availableEquipment.size()) {
                    System.out.println("Invalid choice. Please select a valid equipment number.");
                }
            } else {
                scanner.nextLine();
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        String equipmentId = availableEquipment.get(choice - 1).getEquipmentId();

        // Prompt for date in dd-MM-yyyy format and validate it.
        System.out.print("Enter Reservation Date (dd-MM-yyyy): ");
        String dateInput = scanner.nextLine().trim();
        LocalDate reservationLocalDate;
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            reservationLocalDate = LocalDate.parse(dateInput, inputFormatter);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use dd-MM-yyyy.");
            return;
        }
        LocalDate currentDate = LocalDate.now();
        if (reservationLocalDate.isBefore(currentDate)) {
            System.out.println("Reservation date cannot be before the current date.");
            return;
        }
        Date reservationDate = Date.valueOf(reservationLocalDate);
        boolean success = reservationController.requestReservation(userId, equipmentId, reservationDate);
        if (success) {
            System.out.println("Reservation request submitted successfully!");
        } else {
            System.out.println("Reservation request failed.");
        }
    }

    /**
     * Displays the reservations of the logged-in user.
     *
     * @param scanner      the Scanner to read input.
     * @param loggedInUser the logged-in user.
     */
    public void viewUserReservations(Scanner scanner, User loggedInUser) {
        System.out.println("\n=== View Your Reservations ===");
        String userId = loggedInUser.getUserId();
        try {
            List<Reservation> reservations = reservationController.getUserReservations(userId);
            if (reservations.isEmpty()) {
                System.out.println("You have no reservations.");
                return;
            }
            System.out.println("\nYour Reservations:");
            for (Reservation res : reservations) {
                System.out.println(res.getReservationId() + " | " + res.getEquipmentId() +
                        " | " + res.getReservationDate() + " | " + res.getStatus());
            }
        } catch (Exception e) {
            System.out.println("An error occurred while retrieving reservations: " + e.getMessage());
        }
    }

    /**
     * Manages equipment reservations by allowing an admin to approve or reject them.
     *
     * @param scanner the Scanner to read input.
     */
    public void manageReservations(Scanner scanner) {
        System.out.println("\n=== Manage Equipment Reservations (Admin) ===");
        System.out.print("Enter Admin ID: ");
        String adminId = scanner.nextLine();
        try {
            List<Reservation> reservations = reservationController.getAllReservations(adminId);
            if (reservations.isEmpty()) {
                System.out.println("No pending reservations found.");
                return;
            }
            System.out.println("\nPending Reservations:");
            for (Reservation res : reservations) {
                System.out.println(res.getReservationId() + " | " + res.getUserId() +
                        " | " + res.getEquipmentId() + " | " + res.getStatus());
            }
            System.out.print("\nEnter Reservation ID to update (or 0 to cancel): ");
            int reservationId = scanner.nextInt();
            scanner.nextLine();
            if (reservationId == 0) return;
            System.out.print("Approve or Reject? (A/R): ");
            String status = scanner.nextLine().equalsIgnoreCase("A") ? "Approved" : "Rejected";
            boolean updated = reservationController.updateReservationStatus(reservationId, status, adminId);
            System.out.println(updated ? "Reservation " + status.toLowerCase() + " successfully!" : "Failed to update reservation status.");
        } catch (Exception e) {
            System.out.println("An error occurred while managing reservations: " + e.getMessage());
        }
    }

    /**
     * Displays all reservations related to the logged-in user.
     *
     * @param loggedInUser the logged-in user.
     */
    public void viewReservations(User loggedInUser) {
        try {
            System.out.println("\n===== List of Reservations =====");
            List<Reservation> reservations = reservationController.getAllReservations(loggedInUser.getUserId());
            if (reservations.isEmpty()) {
                System.out.println("No reservations found.");
                return;
            }
            System.out.printf("%-10s %-25s %-25s %-15s %-15s %-15s\n",
                    "Res. ID", "User", "Equipment", "Res. Date", "Return Date", "Status");
            System.out.println("-------------------------------------------------------------------------------------");
            for (Reservation reservation : reservations) {
                System.out.printf("%-10d %-25s %-25s %-15s %-15s %-15s\n",
                        reservation.getReservationId(), reservation.getUserId(),
                        reservation.getEquipmentId(), reservation.getReservationDate(),
                        reservation.getReturnDate(), reservation.getStatus());
            }
        } catch (Exception e) {
            System.out.println("An error occurred while displaying reservations: " + e.getMessage());
        }
    }

    /**
     * Processes equipment check-out.
     *
     * @param scanner the Scanner to read input.
     */
    public void checkOutEquipment(Scanner scanner) {
        System.out.println("\n=== Equipment Check-Out ===");
        List<String> pendingReservations = checkoutController.getPendingCheckouts();
        if (pendingReservations.isEmpty()) {
            System.out.println("No equipment is pending check-out.");
            return;
        }
        System.out.println("Select a reservation to check out:");
        System.out.println("--------------------------------------------------");
        System.out.println("ID | User | Equipment | Reservation Date");
        System.out.println("--------------------------------------------------");
        for (int i = 0; i < pendingReservations.size(); i++) {
            System.out.println((i + 1) + ". " + pendingReservations.get(i));
        }
        System.out.println("--------------------------------------------------");
        int choice = InputValidator.getInt(scanner, "Enter the number of the reservation to check out: ");
        while (choice < 1 || choice > pendingReservations.size()) {
            System.out.println("Invalid choice. Please enter a valid number.");
            choice = InputValidator.getInt(scanner, "Enter the number of the reservation to check out: ");
        }
        int reservationId = Integer.parseInt(pendingReservations.get(choice - 1).split(" \\| ")[0]);
        System.out.print("Enter your Staff ID: ");
        String staffId = scanner.nextLine();
        if (checkoutController.checkOutEquipment(reservationId, staffId)) {
            System.out.println("Equipment successfully checked out.");
        } else {
            System.out.println("Failed to check out equipment.");
        }
    }

    /**
     * Processes equipment check-in using InputValidator for condition validation.
     *
     * @param scanner the Scanner to read input.
     */
    public void checkInEquipment(Scanner scanner) {
        System.out.println("\n=== Equipment Check-In ===");
        List<String> checkedOutList = checkoutController.getCheckedOutEquipment();
        if (checkedOutList.isEmpty()) {
            System.out.println("No equipment is currently checked out.");
            return;
        }
        System.out.println("Select an equipment to check in:");
        System.out.println("--------------------------------------------------");
        System.out.println("ID | User | Equipment | Checked-Out Date");
        System.out.println("--------------------------------------------------");
        for (int i = 0; i < checkedOutList.size(); i++) {
            System.out.println((i + 1) + ". " + checkedOutList.get(i));
        }
        System.out.println("--------------------------------------------------");
        int choice = InputValidator.getInt(scanner, "Enter the number of the equipment to check in: ");
        while (choice < 1 || choice > checkedOutList.size()) {
            System.out.println("Invalid choice. Please enter a valid number.");
            choice = InputValidator.getInt(scanner, "Enter the number of the equipment to check in: ");
        }
        int reservationId = Integer.parseInt(checkedOutList.get(choice - 1).split(" \\| ")[0]);
        System.out.print("Enter your Staff ID: ");
        String staffId = scanner.nextLine();
        String equipmentState = InputValidator.getValidatedEquipmentCondition(scanner, "Enter Equipment State (Good, Fair, Poor): ");
        if (checkoutController.checkInEquipment(reservationId, staffId, equipmentState)) {
            System.out.println("Equipment successfully checked in.");
        } else {
            System.out.println("Failed to check in equipment.");
        }
    }

    // ====================================================
    // ================ Prompt Methods ====================
    // (These methods are used internally to prompt the user for various inputs)
    // ====================================================

    /**
     * Prompts the user to select an equipment type.
     *
     * @param scanner the Scanner to read input.
     * @return the selected equipment type.
     */
    private String promptForEquipmentType(Scanner scanner) {
        String[] equipmentTypes = {"Audio Recorder", "Camera", "Drone", "Laptop", "Lighting", "Projector", "VR Headset", "Other"};
        System.out.println("\nSelect Equipment Type:");
        for (int i = 0; i < equipmentTypes.length; i++) {
            System.out.println((i + 1) + ". " + equipmentTypes[i]);
        }
        int choice = -1;
        while (choice < 1 || choice > equipmentTypes.length) {
            System.out.print("Enter the number corresponding to the equipment type: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice < 1 || choice > equipmentTypes.length) {
                    System.out.println("Invalid selection. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
        return equipmentTypes[choice - 1];
    }

    /**
     * Prompts the user to select a role.
     *
     * @param scanner the Scanner to read input.
     * @return the selected role.
     */
    public String promptForRole(Scanner scanner) {
        String[] roles = {"Student", "Lecturer", "Admin", "MediaStaff"};
        int choice = -1;
        while (choice < 1 || choice > roles.length) {
            try {
                System.out.println("Select a role:");
                for (int i = 0; i < roles.length; i++) {
                    System.out.printf("%d. %s\n", i + 1, roles[i]);
                }
                System.out.print("Enter your choice (1-" + roles.length + "): ");
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    if (choice < 1 || choice > roles.length) {
                        System.out.println("Invalid choice. Please select a number between 1 and " + roles.length + ".");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number between 1 and " + roles.length + ".");
                    scanner.nextLine();
                }
            } catch (Exception e) {
                System.out.println("Error selecting role: " + e.getMessage());
                scanner.nextLine();
            }
        }
        return roles[choice - 1];
    }

    /**
     * Prompts for a valid year between 1 and 4.
     *
     * @param scanner the Scanner to read input.
     * @return the valid year.
     */
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

    /**
     * Prompts the user to select a department.
     *
     * @param scanner the Scanner to read input.
     * @return the selected department.
     */
    public String promptForDepartment(Scanner scanner) {
        String[] departments = {
                "Sport Management", "Architecture", "Arts & Social Studies", "Built Environment", "Business",
                "Computing", "Engineering", "Hospitality, Tourism & Culinary Arts", "Early Years Education", "Media",
                "Design & Music", "Nursing", "Health & Psychology", "Science", "Law", "Sport Science"
        };
        int choice = -1;
        while (choice < 1 || choice > departments.length) {
            try {
                System.out.println("Select a department:");
                for (int i = 0; i < departments.length; i++) {
                    System.out.printf("%d. %s\n", i + 1, departments[i]);
                }
                System.out.print("Enter your choice (1-" + departments.length + "): ");
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    if (choice < 1 || choice > departments.length) {
                        System.out.println("Invalid choice. Please select a number between 1 and " + departments.length + ".");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine();
                }
            } catch (Exception e) {
                System.out.println("Error selecting department: " + e.getMessage());
                scanner.nextLine();
            }
        }
        return departments[choice - 1];
    }

    /**
     * Overloaded method for department selection with the current department provided.
     *
     * @param scanner          the Scanner to read input.
     * @param currentDepartment the current department.
     * @return the new department or the current department if none is entered.
     */
    public String promptForDepartment(Scanner scanner, String currentDepartment) {
        String[] departments = {
                "Sport Management", "Architecture", "Arts & Social Studies", "Built Environment", "Business",
                "Computing", "Engineering", "Hospitality, Tourism & Culinary Arts", "Early Years Education", "Media",
                "Design & Music", "Nursing", "Health & Psychology", "Science", "Law", "Sport Science"
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

    /**
     * Prompts for a course based on the given department.
     *
     * @param scanner    the Scanner to read input.
     * @param department the department to determine available courses.
     * @return the selected course.
     */
    private String promptForCourse(Scanner scanner, String department) {
        String[] courses = getCoursesForDepartment(department);
        if (courses.length == 0) {
            return "";
        }
        System.out.println("Select a course:");
        for (int i = 0; i < courses.length; i++) {
            System.out.printf("%d. %s\n", i + 1, courses[i]);
        }
        int choice = -1;
        while (choice < 1 || choice > courses.length) {
            System.out.print("Enter your choice (1-" + courses.length + "): ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice < 1 || choice > courses.length) {
                    System.out.println("Invalid choice, please try again.");
                }
            } else {
                System.out.println("Invalid input, please enter a number.");
                scanner.nextLine();
            }
        }
        return courses[choice - 1];
    }

    /**
     * Overloaded method for course selection when updating a student.
     * Allows pressing Enter to keep the current course.
     *
     * @param scanner       the Scanner to read input.
     * @param department    the department to determine available courses.
     * @param currentCourse the current course.
     * @return the new course or the current course if no input is given.
     */
    private String promptForCourse(Scanner scanner, String department, String currentCourse) {
        String[] courses = getCoursesForDepartment(department);
        if (courses.length == 0) {
            return currentCourse;
        }
        System.out.println("Select a course (or press Enter to keep current [" + currentCourse + "]):");
        for (int i = 0; i < courses.length; i++) {
            System.out.printf("%d. %s\n", i + 1, courses[i]);
        }
        while (true) {
            System.out.print("Enter your choice (1-" + courses.length + ") or press Enter: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return currentCourse;
            }
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= courses.length) {
                    return courses[choice - 1];
                } else {
                    System.out.println("Invalid choice, please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a valid number.");
            }
        }
    }

    /**
     * Returns an array of courses based on the provided department.
     *
     * @param department the department.
     * @return an array of available courses.
     */
    private String[] getCoursesForDepartment(String department) {
        switch (department) {
            case "Sport Management":
                return new String[]{
                        "Sport Business Management",
                        "Sports Development and Event Management",
                        "Sport Marketing and Sponsorship",
                        "Sports Facility and Venue Management",
                        "Global Sport Governance"
                };
            case "Architecture":
                return new String[]{
                        "Architectural Technology",
                        "Environmental Design",
                        "Urban Planning and Design",
                        "Sustainable Building Design",
                        "Construction and Architectural Engineering"
                };
            case "Arts & Social Studies":
                return new String[]{
                        "Sociology",
                        "Cultural Studies",
                        "Fine Arts: Painting and Drawing",
                        "Performing Arts: Acting and Theatre",
                        "Psychology in Society"
                };
            case "Built Environment":
                return new String[]{
                        "Construction Management",
                        "Quantity Surveying",
                        "Real Estate and Property Development",
                        "Facilities Management",
                        "Building Information Modelling (BIM)"
                };
            case "Business":
                return new String[]{
                        "Business Administration",
                        "Marketing Strategy",
                        "Finance and Investment",
                        "Human Resource Management",
                        "International Trade and Commerce"
                };
            case "Computing":
                return new String[]{
                        "Software Development",
                        "Cybersecurity and Networking",
                        "Data Science and Analytics",
                        "Artificial Intelligence and Machine Learning",
                        "Web and Mobile Application Development"
                };
            case "Engineering":
                return new String[]{
                        "Mechanical Engineering",
                        "Electrical and Electronic Engineering",
                        "Civil Engineering",
                        "Manufacturing and Production Engineering",
                        "Renewable Energy Systems"
                };
            case "Hospitality, Tourism & Culinary Arts":
                return new String[]{
                        "Hotel and Resort Management",
                        "Tourism Destination Management",
                        "Event Planning and Coordination",
                        "Culinary Arts and Gastronomy",
                        "Wine and Beverage Management"
                };
            case "Early Years Education":
                return new String[]{
                        "Early Childhood Care and Education",
                        "Child Development and Psychology",
                        "Inclusive Practices in Early Years",
                        "Play and Learning in Early Childhood",
                        "Leadership in Early Years Settings"
                };
            case "Media":
                return new String[]{
                        "Journalism and News Writing",
                        "Film and Video Production",
                        "Digital Media and Social Media Management",
                        "Public Relations and Advertising",
                        "Broadcast Media and Television Production"
                };
            case "Design & Music":
                return new String[]{
                        "Graphic Design and Visual Communication",
                        "Interior Design and Space Planning",
                        "Music Production and Sound Engineering",
                        "Fashion Design and Textiles",
                        "Game Design and Animation"
                };
            case "Nursing":
                return new String[]{
                        "General Nursing",
                        "Midwifery",
                        "Community Health Nursing",
                        "Mental Health Nursing",
                        "Pediatric Nursing"
                };
            case "Health & Psychology":
                return new String[]{
                        "Health Sciences",
                        "Occupational Therapy",
                        "Clinical Psychology",
                        "Counseling and Psychotherapy",
                        "Nutrition and Dietetics"
                };
            case "Science":
                return new String[]{
                        "Biology",
                        "Chemistry",
                        "Physics",
                        "Environmental Science",
                        "Biotechnology and Genetics"
                };
            case "Law":
                return new String[]{
                        "Legal Studies",
                        "Business Law",
                        "Criminal Justice",
                        "Human Rights Law",
                        "Intellectual Property Law"
                };
            case "Sport Science":
                return new String[]{
                        "Exercise Physiology",
                        "Sports Coaching and Performance",
                        "Physical Education and School Sport",
                        "Sports Injury Rehabilitation",
                        "Strength and Conditioning"
                };
            default:
                return new String[0];
        }
    }
}
