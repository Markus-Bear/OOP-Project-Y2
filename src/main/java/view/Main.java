package view;

import controller.EquipmentController;
import controller.UserController;
import model.Equipment;
import model.User;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final UserController USER_CONTROLLER = new UserController();
    private static final EquipmentController EQUIPMENT_CONTROLLER = new EquipmentController();

    // Display all users
    private static void displayAllUsers() {
        System.out.println("\n========== All Users ==========");
        List<User> users = USER_CONTROLLER.getAllUsers("Admin");

        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        System.out.printf("%-10s %-25s %-25s %-15s\n", "User ID", "Email", "Name", "Role");
        System.out.println("---------------------------------------------------------------------------------------");

        for (User user : users) {
            System.out.printf("%-10s %-25s %-25s %-15s\n",
                    user.getUserId(), user.getEmail(), user.getName(), user.getRole());
        }
    }

    // Display all equipment
    private static void displayAllEquipment(){
        System.out.println("\n========== All Equipment ==========");
        List<Equipment> equipmentList = EQUIPMENT_CONTROLLER.getAllEquipment("Admin");

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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. View All Users");
            System.out.println("2. View All Equipment");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> displayAllUsers();
                case 2 -> displayAllEquipment();
                case 3 -> {
                    System.out.println("Exiting...");
                    return; // Exit the program
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
