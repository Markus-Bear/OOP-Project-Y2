package exception;

/**
 * This exception is thrown when there is a conflict with an existing reservation,
 * such as when a reservation cannot be created because the equipment is already reserved.
 */
public class ReservationConflictException extends Exception {

    /**
     * Constructs a new ReservationConflictException with the specified detail message.
     *
     * @param message the detail message explaining the conflict.
     */
    public ReservationConflictException(String message) {
        super(message);
    }
}
