package controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MethodsUtilTest {

    @Test
    public void testGetDepartments() {
        String[] departments = MethodsUtil.getDepartments();
        assertNotNull(departments);
        assertTrue(departments.length > 0);
    }

    @Test
    public void testGetCoursesForDepartment() {
        String department = "Computing";
        String[] courses = MethodsUtil.getCoursesForDepartment(department);
        assertNotNull(courses);
        assertTrue(courses.length > 0);
    }

    @Test
    public void testGetCoursesForUnknownDepartment() {
        String department = "NonExistingDept";
        String[] courses = MethodsUtil.getCoursesForDepartment(department);
        assertNotNull(courses);
        assertEquals(0, courses.length);
    }
}
