package controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import exception.InvalidInputException;

public class InputValidatorTest {

    @Test
    public void testValidStudentEmail() throws InvalidInputException {
        String email = "C001234@setu.ie";
        String result = InputValidator.validateEmail(email, "Student");
        // For students, the email is returned in lower case
        assertEquals(email, result);
    }

    @Test
    public void testInvalidStudentEmail() {
        String email = "john.doe@setu.ie";
        Exception exception = assertThrows(InvalidInputException.class, () -> {
            InputValidator.validateEmail(email, "Student");
        });
        assertTrue(exception.getMessage().contains("Student email must start with 'C00'"));
    }

    @Test
    public void testValidNonStudentEmail() throws InvalidInputException {
        String email = "john.doe@setu.ie";
        String result = InputValidator.validateEmail(email, "Lecturer");
        assertEquals(email.toLowerCase(), result);
    }

    @Test
    public void testInvalidNonStudentEmail() {
        String email = "johndoe@setu.ie";
        Exception exception = assertThrows(InvalidInputException.class, () -> {
            InputValidator.validateEmail(email, "Lecturer");
        });
        assertTrue(exception.getMessage().contains("Email for this role must be in the format"));
    }

    @Test
    public void testValidateNameValid() throws InvalidInputException {
        String name = "John Doe";
        String result = InputValidator.validateName(name);
        assertEquals(name, result);
    }

    @Test
    public void testValidateNameInvalid() {
        String name = "John123";
        assertThrows(InvalidInputException.class, () -> {
            InputValidator.validateName(name);
        });
    }

    @Test
    public void testValidateEquipmentNameValid() throws InvalidInputException {
        String eqName = "Camera 123";
        String result = InputValidator.validateEquipmentName(eqName);
        assertEquals(eqName, result);
    }

    @Test
    public void testValidateEquipmentNameInvalid() {
        String eqName = "Camera@123";
        assertThrows(InvalidInputException.class, () -> {
            InputValidator.validateEquipmentName(eqName);
        });
    }

    @Test
    public void testValidateEquipmentDescriptionValid() throws InvalidInputException {
        String description = "High quality camera";
        String result = InputValidator.validateEquipmentDescription(description);
        assertEquals(description, result);
    }

    @Test
    public void testValidateEquipmentDescriptionInvalid() {
        String description = "Camera!!!";
        assertThrows(InvalidInputException.class, () -> {
            InputValidator.validateEquipmentDescription(description);
        });
    }
}
