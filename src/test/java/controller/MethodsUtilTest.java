package controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
/**
 * Unit tests for the MethodsUtil class.
 *
 * <p>
 * These tests check that the getDepartments() method returns a non-empty list,
 * and that getCoursesForDepartment(String) returns an expected set of courses.
 * </p>
 */
public class MethodsUtilTest {

    @Test
    public void testGetDepartments() {
        String[] departments = MethodsUtil.getDepartments();
        assertNotNull(departments);
        assertTrue(departments.length > 0);
    }


    @Test
    public void testGetCoursesForUnknownDepartment() {
        String department = "NonExistingDept";
        String[] courses = MethodsUtil.getCoursesForDepartment(department);
        assertNotNull(courses);
        assertEquals(0, courses.length);
    }

    @Test
    public void testGetDepartmentsNotEmpty() {
        String[] departments = MethodsUtil.getDepartments();
        assertNotNull(departments);
        assertTrue(departments.length > 0, "Departments should not be empty.");
    }

    @Test
    public void testGetCoursesForDepartment() {
        String[] mediaCourses = MethodsUtil.getCoursesForDepartment("Media");
        assertNotNull(mediaCourses);
        assertTrue(mediaCourses.length > 0, "Media courses should not be empty.");
        boolean foundExpected = false;
        for (String course : mediaCourses) {
            if (course.contains("Journalism")) {
                foundExpected = true;
                break;
            }
        }
        assertTrue(foundExpected, "Expected course for Media should be present.");
    }
}
