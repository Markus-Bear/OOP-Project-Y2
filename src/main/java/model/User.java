package model;

/**
 * Represents a user of the system.
 * A User has a unique ID, email, name, password, role, and additional role-specific attributes
 * such as department, course, and year.
 */
public class User {
    private String userId;       // Unique ID (e.g., C001, L001)
    private String email;        // User's email address
    private String name;         // User's full name
    private String password;     // User's password
    private String role;         // Role (Student, Lecturer, Admin, MediaStaff) HAVE TO SEPARATE OUT INTO SUB CLASSES
    private String department;   // Department (For Lecturers, Students)
    private String course;       // Course (For Students)
    private Integer year;        // Year (For Students)

    /**
     * Constructs a User with the specified details.
     *
     * @param userId     the unique user ID
     * @param email      the user's email address
     * @param name       the user's full name
     * @param password   the user's password
     * @param role       the user's role (Student, Lecturer, Admin, MediaStaff)
     * @param department the user's department (if applicable)
     * @param course     the user's course (if applicable)
     * @param year       the user's year (if applicable)
     */
    public User(String userId, String email, String name, String password, String role, String department, String course, Integer year) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.department = department;
        this.course = course;
        this.year = year;
    }

    /**
     * Constructs an empty User.
     * All String fields are initialized to empty strings and year is set to null.
     */
    public User() {
        this.userId = "";
        this.email = "";
        this.name = "";
        this.password = "";
        this.role = "";
        this.department = "";
        this.course = "";
        this.year = null;
    }

    /**
     * Returns the user ID.
     *
     * @return the user ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     *
     * @param userId the user ID to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Returns the user's email address.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the user's full name.
     *
     * @return the full name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's full name.
     *
     * @param name the full name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the user's password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the user's role.
     *
     * @return the role (Student, Lecturer, Admin, MediaStaff)
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the user's role.
     *
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Returns the user's department.
     *
     * @return the department (if applicable)
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the user's department.
     *
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Returns the user's course.
     *
     * @return the course (if applicable)
     */
    public String getCourse() {
        return course;
    }

    /**
     * Sets the user's course.
     *
     * @param course the course to set
     */
    public void setCourse(String course) {
        this.course = course;
    }

    /**
     * Returns the user's year.
     *
     * @return the year (if applicable)
     */
    public Integer getYear() {
        return year;
    }

    /**
     * Sets the user's year.
     *
     * @param year the year to set
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * Checks if the user object is valid.
     * A user is considered valid if email, password, name, and role are non-null and not empty.
     *
     * @return true if the user is valid, false otherwise.
     */
    public boolean isValid() {
        return email != null && !email.isEmpty() &&
                password != null && !password.isEmpty() &&
                name != null && !name.isEmpty() &&
                role != null && !role.isEmpty();
    }
}
