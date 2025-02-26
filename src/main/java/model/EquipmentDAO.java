package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.DatabaseConnection;
import exception.DatabaseOperationException;


public class EquipmentDAO {

    // Fetch all equipment
    public List<Equipment> getAllEquipment() throws DatabaseOperationException {
        String query = "SELECT * FROM Equipment";
        List<Equipment> equipmentList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

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
            throw new DatabaseOperationException("Error fetching all equipment from the database.", e);
        }

        return equipmentList;
    }

    public List<Equipment> getEquipmentByStatus(String status) throws DatabaseOperationException {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty.");
        }

        List<Equipment> equipmentList = new ArrayList<>();
        String query = "SELECT * FROM Equipment WHERE status = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    equipmentList.add(new Equipment(
                            rs.getString("equipment_id"),
                            rs.getString("name"),
                            rs.getString("type"),
                            rs.getString("description"),
                            rs.getString("status"),
                            rs.getString("state")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching equipment with status: " + status, e);
        }

        return equipmentList;
    }


    // Fetch equipment by type
    public List<Equipment> getEquipmentByType(String type) throws DatabaseOperationException {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment type cannot be null or empty.");
        }

        String query = "SELECT * FROM equipment WHERE type = ?";
        List<Equipment> equipmentList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, type);

            try (ResultSet rs = stmt.executeQuery()) {
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
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching equipment by type: " + type, e);
        }

        return equipmentList;
    }

    public List<Equipment> getEquipmentByTypeAndStatus(String type, String status) throws DatabaseOperationException {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment type cannot be null or empty.");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment status cannot be null or empty.");
        }

        List<Equipment> equipmentList = new ArrayList<>();
        String query = "SELECT * FROM Equipment WHERE type = ? AND status = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, type);
            stmt.setString(2, status);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    equipmentList.add(new Equipment(
                            rs.getString("equipment_id"),
                            rs.getString("name"),
                            rs.getString("type"),
                            rs.getString("description"),
                            rs.getString("status"),
                            rs.getString("state")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching equipment by type: " + type + " and status: " + status, e);
        }

        return equipmentList;
    }

    //Add new equipment
    public boolean addEquipment(Equipment equipment, String userId) throws DatabaseOperationException {
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment object cannot be null.");
        }
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        if (equipment.getName() == null || equipment.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment name cannot be null or empty.");
        }
        if (equipment.getType() == null || equipment.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment type cannot be null or empty.");
        }
        if (equipment.getState() == null || equipment.getState().trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment state cannot be null or empty.");
        }

        String sql = "{CALL AddEquipment(?, ?, ?, ?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, equipment.getName());
            stmt.setString(2, equipment.getType());

            if (equipment.getDescription() != null) {
                stmt.setString(3, equipment.getDescription());
            } else {
                stmt.setString(3, "");
            }

            stmt.setString(4, equipment.getState());
            stmt.setString(5, userId);

            boolean hasResults = stmt.execute();

            if (hasResults) {
                try (ResultSet rs = stmt.getResultSet()) {
                    if (rs.next()) {
                        int rowsAffected = rs.getInt("RowsAffected");
                        return rowsAffected > 0;
                    }
                }
            }

            return false;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error adding new equipment to the database.", e);
        }
    }

    public boolean updateEquipment(Equipment equipment, String requesterId) throws DatabaseOperationException {
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment object cannot be null.");
        }
        if (requesterId == null || requesterId.trim().isEmpty()) {
            throw new IllegalArgumentException("Requester ID cannot be null or empty.");
        }
        if (equipment.getEquipmentId() == null || equipment.getEquipmentId().trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment ID cannot be null or empty.");
        }
        if (equipment.getName() == null || equipment.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment name cannot be null or empty.");
        }
        if (equipment.getType() == null || equipment.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment type cannot be null or empty.");
        }
        if (equipment.getState() == null || equipment.getState().trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment state cannot be null or empty.");
        }
        if (equipment.getStatus() == null || equipment.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment status cannot be null or empty.");
        }

        String sql = "{CALL UpdateEquipment(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, equipment.getEquipmentId());
            stmt.setString(2, equipment.getName());
            stmt.setString(3, equipment.getType());

            if (equipment.getDescription() != null) {
                stmt.setString(4, equipment.getDescription());
            } else {
                stmt.setString(4, "");
            }

            stmt.setString(5, equipment.getState());
            stmt.setString(6, equipment.getStatus());
            stmt.setString(7, requesterId);

            boolean hasResults = stmt.execute();

            if (hasResults) {
                try (ResultSet rs = stmt.getResultSet()) {
                    if (rs.next()) {
                        int rowsAffected = rs.getInt("RowsAffected");
                        return rowsAffected > 0;
                    }
                }
            }

            return false;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating equipment in the database.", e);
        }
    }

    public boolean deleteEquipment(String equipmentId, String userId) throws DatabaseOperationException {
        if (equipmentId == null || equipmentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment ID cannot be null or empty.");
        }
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }

        String sql = "{CALL DeleteEquipment(?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, equipmentId);
            stmt.setString(2, userId);

            boolean hasResults = stmt.execute();

            if (hasResults) {
                try (ResultSet rs = stmt.getResultSet()) {
                    if (rs.next()) {
                        return rs.getInt("RowsAffected") > 0;
                    }
                }
            }

            return false;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting equipment from the database.", e);
        }
    }
}
