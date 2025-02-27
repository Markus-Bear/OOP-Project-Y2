package view;

import controller.LoginManager;
import controller.MethodsUtil;
import model.User;

import java.util.Scanner;

/**
 * The Main class is the entry point for the application.
 * It handles user login and routes the logged-in user to the appropriate menu based on their role.
 */
public class Main {

    /**
     * Main method that creates a Scanner and starts the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        startApplication(scanner);
    }

    /**
     * Starts the application by prompting the user to login and then routing to the appropriate menu.
     *
     * @param scanner the Scanner used to read user input
     */
    public static void startApplication(Scanner scanner) {
        LoginManager loginManager = new LoginManager();
        MethodsUtil methodsUtil = new MethodsUtil();
        User loggedInUser = null;

        // Keep prompting until successful login
        while (loggedInUser == null) {
            loggedInUser = loginManager.loginUser();
        }

        System.out.println("\nWelcome, " + loggedInUser.getName() + "!");

        // Route user to the appropriate menu based on role
        switch (loggedInUser.getRole()) {
            case "Admin" -> adminMenu(scanner, loggedInUser, methodsUtil);
            case "MediaStaff" -> mediaStaffMenu(scanner, loggedInUser, methodsUtil);
            case "Lecturer", "Student" -> userMenu(scanner, loggedInUser, methodsUtil);
            default -> {
                System.out.println("Invalid role detected. Exiting...");
                System.exit(1);
            }
        }
    }

    // ========================= ADMIN MENUS ========================= //

    /**
     * Displays the Admin main menu and routes to the appropriate sub-menu based on user input.
     *
     * @param scanner      the Scanner used to read user input
     * @param loggedInUser the logged-in user
     * @param methodsUtil  an instance of MethodsUtil to perform operations
     */
    public static void adminMenu(Scanner scanner, User loggedInUser, MethodsUtil methodsUtil) {
        while (true) {
            System.out.println("\n===== Admin Menu =====");
            System.out.println("1. View Profile");
            System.out.println("2. User Management");
            System.out.println("3. Equipment Management");
            System.out.println("4. Reservations / Check-Out / Check-In");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> methodsUtil.viewProfile(loggedInUser);
                case 2 -> adminUserManagementMenu(scanner, methodsUtil);
                case 3 -> adminEquipmentManagementMenu(scanner, methodsUtil);
                case 4 -> adminReservationsCheckOutMenu(scanner, loggedInUser, methodsUtil);
                case 5 -> {
                    System.out.println("Exiting... Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the Admin User Management menu for viewing, adding, updating, and deleting users.
     *
     * @param scanner     the Scanner used to read user input
     * @param methodsUtil an instance of MethodsUtil to perform user management operations
     */
    public static void adminUserManagementMenu(Scanner scanner, MethodsUtil methodsUtil) {
        while (true) {
            System.out.println("\n===== Admin - User Management =====");
            System.out.println("1. View All Users");
            System.out.println("2. Add New User");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
            System.out.println("5. Back to Admin Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> methodsUtil.displayAllUsers();
                case 2 -> methodsUtil.addNewUser(scanner);
                case 3 -> methodsUtil.updateUser(scanner);
                case 4 -> methodsUtil.deleteUser(scanner);
                case 5 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the Admin Equipment Management menu for viewing, adding, updating, and deleting equipment.
     *
     * @param scanner     the Scanner used to read user input
     * @param methodsUtil an instance of MethodsUtil to perform equipment management operations
     */
    public static void adminEquipmentManagementMenu(Scanner scanner, MethodsUtil methodsUtil) {
        while (true) {
            System.out.println("\n===== Admin - Equipment Management =====");
            System.out.println("1. View All Equipment");
            System.out.println("2. View Equipment by Type");
            System.out.println("3. Add New Equipment");
            System.out.println("4. Update Equipment");
            System.out.println("5. Delete Equipment");
            System.out.println("6. Back to Admin Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> methodsUtil.displayAllEquipment();
                case 2 -> methodsUtil.viewEquipmentByType(scanner);
                case 3 -> methodsUtil.addNewEquipment(scanner);
                case 4 -> methodsUtil.updateEquipment(scanner);
                case 5 -> methodsUtil.deleteEquipment(scanner);
                case 6 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the Admin Reservations/Check-Out/Check-In menu for managing reservations and equipment check operations.
     *
     * @param scanner      the Scanner used to read user input
     * @param loggedInUser the logged-in user
     * @param methodsUtil  an instance of MethodsUtil to perform reservation operations
     */
    public static void adminReservationsCheckOutMenu(Scanner scanner, User loggedInUser, MethodsUtil methodsUtil) {
        while (true) {
            System.out.println("\n===== Admin - Reservations / Check-Out / Check-In =====");
            System.out.println("1. Approve/Reject Reservations");
            System.out.println("2. Check-Out Equipment");
            System.out.println("3. Check-In Equipment");
            System.out.println("4. Back to Admin Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> methodsUtil.manageReservations(scanner);
                case 2 -> methodsUtil.checkOutEquipment(scanner);
                case 3 -> methodsUtil.checkInEquipment(scanner);
                case 4 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // ========================= MEDIA STAFF MENUS ========================= //

    /**
     * Displays the Media Staff main menu and routes to the appropriate sub-menu.
     *
     * @param scanner      the Scanner used to read user input
     * @param loggedInUser the logged-in user
     * @param methodsUtil  an instance of MethodsUtil to perform operations
     */
    public static void mediaStaffMenu(Scanner scanner, User loggedInUser, MethodsUtil methodsUtil) {
        while (true) {
            System.out.println("\n===== Media Staff Menu =====");
            System.out.println("1. View Profile");
            System.out.println("2. User Management");
            System.out.println("3. Equipment Management");
            System.out.println("4. Reservations / Check-Out / Check-In");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> methodsUtil.viewProfile(loggedInUser);
                case 2 -> methodsUtil.displayLecturersAndStudents();
                case 3 -> mediaStaffEquipmentManagementMenu(scanner, methodsUtil);
                case 4 -> mediaStaffReservationsCheckOutMenu(scanner, loggedInUser, methodsUtil);
                case 5 -> {
                    System.out.println("Exiting... Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the Media Staff Equipment Management menu.
     *
     * @param scanner     the Scanner used to read user input
     * @param methodsUtil an instance of MethodsUtil to perform equipment management operations
     */
    public static void mediaStaffEquipmentManagementMenu(Scanner scanner, MethodsUtil methodsUtil) {
        while (true) {
            System.out.println("\n===== Media Staff - Equipment Management =====");
            System.out.println("1. View All Equipment");
            System.out.println("2. View Equipment by Type");
            System.out.println("3. Back to Media Staff Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> methodsUtil.displayAllEquipment();
                case 2 -> methodsUtil.viewEquipmentByType(scanner);
                case 3 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the Media Staff Reservations/Check-Out/Check-In menu.
     *
     * @param scanner      the Scanner used to read user input
     * @param loggedInUser the logged-in user
     * @param methodsUtil  an instance of MethodsUtil to perform reservation operations
     */
    public static void mediaStaffReservationsCheckOutMenu(Scanner scanner, User loggedInUser, MethodsUtil methodsUtil) {
        while (true) {
            System.out.println("\n===== Media Staff - Reservations / Check-Out / Check-In =====");
            System.out.println("1. View All Reservations");
            System.out.println("2. Check-Out Equipment");
            System.out.println("3. Check-In Equipment");
            System.out.println("4. Back to Media Staff Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> methodsUtil.viewReservations(loggedInUser);
                case 2 -> methodsUtil.checkOutEquipment(scanner);
                case 3 -> methodsUtil.checkInEquipment(scanner);
                case 4 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // ========================= UNIFIED LECTURER & STUDENT MENU ========================= //

    /**
     * Displays a unified menu for Lecturers and Students.
     *
     * @param scanner      the Scanner used to read user input
     * @param loggedInUser the logged-in user
     * @param methodsUtil  an instance of MethodsUtil to perform operations
     */
    public static void userMenu(Scanner scanner, User loggedInUser, MethodsUtil methodsUtil) {
        while (true) {
            System.out.println("\n===== " + loggedInUser.getRole() + " Menu =====");
            System.out.println("1. View Profile");
            System.out.println("2. View All Equipment");
            System.out.println("3. View Equipment by Type");
            System.out.println("4. Request Equipment Reservation");
            System.out.println("5. View Your Reservations");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> methodsUtil.viewProfile(loggedInUser);
                case 2 -> methodsUtil.displayAllAvailableEquipment();
                case 3 -> methodsUtil.viewAvailableEquipmentByType(scanner);
                case 4 -> methodsUtil.requestReservation(scanner, loggedInUser);
                case 5 -> methodsUtil.viewUserReservations(scanner, loggedInUser);
                case 6 -> {
                    System.out.println("Exiting... Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
