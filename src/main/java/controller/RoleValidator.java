package controller;

public class RoleValidator {
    public static void validateRole(String userRole, String... allowedRoles){
        for (String allowedRole : allowedRoles) {
            if (userRole.equals(allowedRole)) {
                return; // Role is valid
            }
        }

    }
}

/*This is for when I re-implement the login page

 public class RoleValidator {
    public static void validateRole(String userRole, String... allowedRoles) {
        for (String allowedRole : allowedRoles) {
            if (userRole != null && userRole.equals(allowedRole)) {
                return;
            }
        }
        throw new SecurityException("Unauthorized access: Invalid role.");
    }
}
* */