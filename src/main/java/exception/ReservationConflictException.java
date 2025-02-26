package exception;

// Custom exception for reservation conflicts
public class ReservationConflictException extends Exception {
    public ReservationConflictException(String message) {
        super(message);
    }
}
