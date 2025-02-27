package controller;

import exception.RoleAccessException;

/**
 * Utility class for validating a user's role against a set of allowed roles.
 */
public class RoleValidator {

    /**
     * Validates whether the provided user role is among the allowed roles.
     * The comparison is case-insensitive.
     *
     * @param userRole     the role of the user to validate
     * @param allowedRoles a varargs list of allowed roles
     * @throws IllegalArgumentException if userRole is null or empty
     * @throws RoleAccessException      if the userRole is not among the allowedRoles
     * @return true if the users role is valid
     */
    public static void validateRole(String userRole, String... allowedRoles) throws RoleAccessException {
        if (userRole == null || userRole.trim().isEmpty()) {
            throw new IllegalArgumentException("User role cannot be null or empty.");
        }

        for (String allowedRole : allowedRoles) {
            if (userRole.equalsIgnoreCase(allowedRole)) {
                return; // Role is valid
            }
        }

        throw new RoleAccessException("Access denied: Role '" + userRole + "' is not authorized for this operation.");
    }
}
