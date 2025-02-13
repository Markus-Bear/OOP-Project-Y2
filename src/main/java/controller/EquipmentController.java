package controller;

import model.Equipment;
import model.EquipmentDAO;

import java.util.List;

public class EquipmentController {
   final  private EquipmentDAO equipmentDAO = new EquipmentDAO();

    // Retrieve all equipment
    public List<Equipment> getAllEquipment(String userRole){
        RoleValidator.validateRole(userRole, "Admin", "MediaStaff", "Student", "Lecturer");

        return equipmentDAO.getAllEquipment();
    }

    //Retrieve equipment by type
    public List<Equipment> getEquipmentByType(String type, String userRole) {
        RoleValidator.validateRole(userRole, "Admin", "MediaStaff", "Student", "Lecturer");
        return equipmentDAO.getEquipmentByType(type);
    }

}
