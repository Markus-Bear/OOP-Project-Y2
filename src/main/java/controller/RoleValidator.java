package controller;

import exception.RoleAccessException;

public class RoleValidator {
    public static void validateRole(String userRole, String... allowedRoles) throws RoleAccessException {
        if (userRole == null || userRole.trim().isEmpty()) {
            throw new IllegalArgumentException("User role cannot be null or empty.");
        }

        for (String allowedRole : allowedRoles) {
            if (userRole.equalsIgnoreCase(allowedRole)) { // Case-insensitive check
                return; // Role is valid
            }
        }

        throw new RoleAccessException("Access denied: Role '" + userRole + "' is not authorized for this operation.");
    }
}
