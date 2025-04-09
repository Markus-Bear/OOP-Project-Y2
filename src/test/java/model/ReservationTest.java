package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Date;

public class ReservationTest {

    @Test
    public void testReservationGettersSetters() {
        Date now = new Date();
        Reservation reservation = new Reservation(1, "U001", "E001", now, now, "Pending");
        assertEquals(1, reservation.getReservationId());
        assertEquals("U001", reservation.getUserId());
        assertEquals("E001", reservation.getEquipmentId());
        assertEquals(now, reservation.getReservationDate());
        assertEquals(now, reservation.getReturnDate());
        assertEquals("Pending", reservation.getStatus());
    }
}
