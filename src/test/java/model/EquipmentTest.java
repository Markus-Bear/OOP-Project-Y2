package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class EquipmentTest {

    @Test
    public void testEquipmentGettersSetters() {
        Equipment equipment = new Equipment();
        equipment.setEquipmentId("E001");
        equipment.setName("Camera");
        equipment.setType("Electronics");
        equipment.setDescription("DSLR Camera");
        equipment.setStatus("Available");
        equipment.setState("New");

        assertEquals("E001", equipment.getEquipmentId());
        assertEquals("Camera", equipment.getName());
        assertEquals("Electronics", equipment.getType());
        assertEquals("DSLR Camera", equipment.getDescription());
        assertEquals("Available", equipment.getStatus());
        assertEquals("New", equipment.getState());
    }
}
