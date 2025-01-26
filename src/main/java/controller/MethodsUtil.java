package controller;

import model.Equipment;
import model.User;

import java.util.List;

public class MethodsUtil {

    private static final UserController USER_CONTROLLER = new UserController();
    private static final EquipmentController EQUIPMENT_CONTROLLER = new EquipmentController();

    // Display all users
    public void displayAllUsers() {
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
    public void displayAllEquipment(){
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
}
