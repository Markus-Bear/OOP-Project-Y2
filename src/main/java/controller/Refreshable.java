package controller;

import exception.DatabaseOperationException;

/**
 * This interface defines a contract for components that support a refresh operation.
 * Classes implementing this interface should provide an implementation for refreshing
 * their content, and may throw a {@link DatabaseOperationException} if the refresh fails
 * due to database issues.
 */
public interface Refreshable {
    /**
     * Refreshes the content of the component.
     *
     * @throws DatabaseOperationException if an error occurs during the refresh operation.
     */
    void refresh() throws DatabaseOperationException;
}
