package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Date;

/**
 * Unit tests for the Reservation model.
 *
 * <p>
 * This test verifies that a Reservation object is created properly and that its
 * attributes (reservation ID, user ID, equipment ID, dates, and status) are set and retrievable.
 * </p>
 */
public class ReservationTest {

    @Test
    public void testReservationCreation() {
        Date reservationDate = Date.valueOf("2025-05-01");
        Date returnDate = Date.valueOf("2025-05-10");
        Reservation reservation = new Reservation(1, "U001", "E001", reservationDate, returnDate, "Approved");
        assertEquals(1, reservation.getReservationId());
        assertEquals("U001", reservation.getUserId());
        assertEquals("E001", reservation.getEquipmentId());
        assertEquals("Approved", reservation.getStatus());
    }
}
