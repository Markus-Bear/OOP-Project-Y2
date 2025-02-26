package model;

public class Equipment {
    private String equipmentId;
    private String name;
    private String type;
    private String description;
    private String status;
    private String state;

    public Equipment(String equipmentId, String name, String type, String description, String status, String state) {
        this.equipmentId = equipmentId;
        this.name = name;
        this.type = type;
        this.description = description;
        this.status = status;
        this.state = state;
    }

    public Equipment() {
        this.equipmentId = "";
        this.name = "";
        this.type = "";
        this.description = "";
        this.status = "";
        this.state = "";
    }


    // Getters and Setters
    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
