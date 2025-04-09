package controller;

import model.Equipment;
import model.EquipmentDAO;
import model.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for the EquipmentController class.
 *
 * <p>
 * These tests verify that EquipmentController correctly enforces role validation and calls
 * the underlying DAO methods. Mockito is used to simulate DAO responses.
 * </p>
 */
public class EquipmentControllerTest {

    private EquipmentDAO equipmentDAOMock;
    private UserDAO userDAOMock;
    private EquipmentController equipmentController;

    @BeforeEach
    public void setUp() throws Exception {
        equipmentDAOMock = mock(EquipmentDAO.class);
        userDAOMock = mock(UserDAO.class);

        // Create a subclass of EquipmentController that uses the mocks.
        equipmentController = new EquipmentController() {
            {
                // Override the DAO fields using reflection.
                java.lang.reflect.Field equipmentDAOField = EquipmentController.class.getDeclaredField("equipmentDAO");
                equipmentDAOField.setAccessible(true);
                equipmentDAOField.set(this, equipmentDAOMock);

                java.lang.reflect.Field userDAOField = EquipmentController.class.getDeclaredField("userDAO");
                userDAOField.setAccessible(true);
                userDAOField.set(this, userDAOMock);
            }
        };
    }

    @Test
    public void testGetAllEquipmentValidRole() throws Exception {
        // Stub the DAO to return a sample equipment list.
        List<Equipment> equipmentList = Arrays.asList(
                new Equipment("E001", "Camera", "Electronics", "DSLR camera", "Available", "Good")
        );
        when(equipmentDAOMock.getAllEquipment()).thenReturn(equipmentList);
        List<Equipment> result = equipmentController.getAllEquipment("Admin");
        assertEquals(equipmentList, result, "Returned equipment list should match the stubbed list.");
    }

    @Test
    public void testGetAllEquipmentInvalidRole() {
        // When using an invalid role, the controller should return an empty list.
        List<Equipment> result = equipmentController.getAllEquipment("Student");
        assertTrue(result.isEmpty(), "Invalid role should lead to an empty equipment list.");
    }
}
