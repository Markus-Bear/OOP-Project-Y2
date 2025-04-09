package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Equipment model.
 *
 * <p>
 * These tests confirm that the Equipment class’s getters and setters correctly
 * store and retrieve information.
 * </p>
 */
public class EquipmentTest {

    @Test
    public void testEquipmentGettersSetters() {
        Equipment equipment = new Equipment("E001", "Camera", "Electronics", "High quality DSLR", "Available", "New");
        assertEquals("E001", equipment.getEquipmentId());
        assertEquals("Camera", equipment.getName());
        equipment.setName("DSLR Camera");
        assertEquals("DSLR Camera", equipment.getName());
        equipment.setStatus("Reserved");
        assertEquals("Reserved", equipment.getStatus());
    }
}
