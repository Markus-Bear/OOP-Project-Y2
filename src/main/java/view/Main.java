package view;

import controller.MethodsUtil;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MethodsUtil methodsUtil = new MethodsUtil();
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. View All Users");
            System.out.println("2. View All Equipment");
            System.out.println("3. Add New User");
            System.out.println("4. Update User");
            System.out.println("5. Delete User");
            System.out.println("6. View Equipment by Type");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> methodsUtil.displayAllUsers();
                case 2 -> methodsUtil.displayAllEquipment();
                case 3 -> methodsUtil.addNewUser(scanner);
                case 4 -> methodsUtil.updateUser(scanner);
                case 5 -> methodsUtil.deleteUser(scanner);
                case 6 -> methodsUtil.viewEquipmentByType(scanner);
                case 7 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}