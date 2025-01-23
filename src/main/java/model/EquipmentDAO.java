package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.DatabaseConnection;

public class EquipmentDAO {

    // Fetch all equipment
    public List<Equipment> getAllEquipment() {
        String query = "SELECT * FROM Equipment";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Equipment> equipmentList = new ArrayList<>();
        try {
            conn = DatabaseConnection.getConnection(); // Get connection from DatabaseUtils
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Equipment equipment = new Equipment();
                equipment.setEquipmentId(rs.getString("equipment_id"));
                equipment.setName(rs.getString("name"));
                equipment.setType(rs.getString("type"));
                equipment.setDescription(rs.getString("description"));
                equipment.setStatus(rs.getString("status"));
                equipment.setState(rs.getString("state"));
                equipmentList.add(equipment);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all equipment: " + e.getMessage());
        } finally {
            try {
                DatabaseConnection.closeResources(conn, stmt, rs);
            } catch (Exception e) {
                e.printStackTrace();
            }        }

        return equipmentList;
    }
}
