package model;

import exception.DatabaseOperationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for performing operations on Equipment data.
 * Provides methods for fetching, adding, updating, and deleting equipment records.
 */
public class EquipmentDAO {

    /**
     * Retrieves all equipment from the database.
     *
     * @return a List of Equipment objects representing all equipment.
     * @throws DatabaseOperationException if a database error occurs.
     */
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

    /**
     * Retrieves equipment filtered by a specific status.
     *
     * @param status the equipment status to filter by.
     * @return a List of Equipment objects that have the specified status.
     * @throws DatabaseOperationException if a database error occurs.
     * @throws IllegalArgumentException   if status is null or empty.
     */
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

    /**
     * Retrieves equipment filtered by a specific type.
     *
     * @param type the equipment type to filter by.
     * @return a List of Equipment objects that have the specified type.
     * @throws DatabaseOperationException if a database error occurs.
     * @throws IllegalArgumentException   if type is null or empty.
     */
    public List<Equipment> getEquipmentByType(String type) throws DatabaseOperationException {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment type cannot be null or empty.");
        }

        String query = "SELECT * FROM Equipment WHERE type = ?";
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

    /**
     * Retrieves equipment filtered by type and status.
     *
     * @param type   the equipment type to filter by.
     * @param status the equipment status to filter by.
     * @return a List of Equipment objects that match the specified type and status.
     * @throws DatabaseOperationException if a database error occurs.
     * @throws IllegalArgumentException   if type or status is null or empty.
     */
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

    /**
     * Adds a new equipment record to the database using a stored procedure.
     *
     * @param equipment the Equipment object containing equipment details.
     * @param userId    the ID of the user adding the equipment.
     * @return true if the equipment is added successfully; false otherwise.
     * @throws DatabaseOperationException if a database error occurs.
     * @throws IllegalArgumentException   if equipment or required fields are invalid.
     */
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
             CallableStatement stmt = conn.prepareCall(sql)) {

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

    /**
     * Updates an existing equipment record using a stored procedure.
     *
     * @param equipment   the Equipment object with updated details.
     * @param requesterId the ID of the user requesting the update.
     * @return true if the equipment is updated successfully; false otherwise.
     * @throws DatabaseOperationException if a database error occurs.
     * @throws IllegalArgumentException   if equipment or required fields are invalid.
     */
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
             CallableStatement stmt = conn.prepareCall(sql)) {

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

    /**
     * Deletes an equipment record using a stored procedure.
     *
     * @param equipmentId the ID of the equipment to delete.
     * @param userId      the ID of the user requesting deletion.
     * @return true if the equipment is deleted successfully; false otherwise.
     * @throws DatabaseOperationException if a database error occurs.
     * @throws IllegalArgumentException   if equipmentId or userId is null or empty.
     */
    public boolean deleteEquipment(String equipmentId, String userId) throws DatabaseOperationException {
        if (equipmentId == null || equipmentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment ID cannot be null or empty.");
        }
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }

        String sql = "{CALL DeleteEquipment(?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

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
