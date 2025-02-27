package model;

/**
 * Represents a piece of equipment in the system.
 * Contains details such as equipment ID, name, type, description, status, and state.
 */
public class Equipment {
    private String equipmentId;
    private String name;
    private String type;
    private String description;
    private String status;
    private String state;

    /**
     * Constructs an Equipment object with the specified details.
     *
     * @param equipmentId the unique identifier for the equipment
     * @param name        the name of the equipment
     * @param type        the type of the equipment
     * @param description a brief description of the equipment
     * @param status      the status of the equipment (e.g., Available, Reserved, CheckedOut)
     * @param state       the current condition of the equipment (e.g., New, Good, Fair, Poor)
     */
    public Equipment(String equipmentId, String name, String type, String description, String status, String state) {
        this.equipmentId = equipmentId;
        this.name = name;
        this.type = type;
        this.description = description;
        this.status = status;
        this.state = state;
    }

    /**
     * Constructs an Equipment object with default empty values.
     */
    public Equipment() {
        this.equipmentId = "";
        this.name = "";
        this.type = "";
        this.description = "";
        this.status = "";
        this.state = "";
    }

    /**
     * Returns the equipment ID.
     *
     * @return the equipment ID.
     */
    public String getEquipmentId() {
        return equipmentId;
    }

    /**
     * Sets the equipment ID.
     *
     * @param equipmentId the equipment ID to set.
     */
    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    /**
     * Returns the name of the equipment.
     *
     * @return the equipment name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the equipment name.
     *
     * @param name the equipment name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the type of the equipment.
     *
     * @return the equipment type.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the equipment type.
     *
     * @param type the equipment type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the description of the equipment.
     *
     * @return the equipment description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the equipment description.
     *
     * @param description the description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the status of the equipment.
     *
     * @return the equipment status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the equipment status.
     *
     * @param status the status to set (e.g., Available, Reserved, CheckedOut).
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns the state of the equipment.
     *
     * @return the equipment state (e.g., New, Good, Fair, Poor).
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the equipment state.
     *
     * @param state the state to set (e.g., New, Good, Fair, Poor).
     */
    public void setState(String state) {
        this.state = state;
    }
}
