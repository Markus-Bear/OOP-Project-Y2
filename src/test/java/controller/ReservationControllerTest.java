package controller;

import model.ReservationDAO;
import model.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationControllerTest {

    @Mock
    private UserDAO userDAOMock;

    @Mock
    private ReservationDAO reservationDAOMock;

    private ReservationController reservationController;

    @BeforeEach
    public void setUp() throws Exception {
        // Instantiate the controller normally.
        reservationController = new ReservationController();

        // Override the 'userDAO' field
        Field userDAOField = ReservationController.class.getDeclaredField("userDAO");
        userDAOField.setAccessible(true);
        userDAOField.set(reservationController, userDAOMock);

        // Override the 'reservationDAO' field
        Field reservationDAOField = ReservationController.class.getDeclaredField("reservationDAO");
        reservationDAOField.setAccessible(true);
        reservationDAOField.set(reservationController, reservationDAOMock);
    }

    @Test
    public void testRequestReservationWithValidData() throws Exception {
        String userId = "C00123";
        String equipmentId = "E001";
        Date reservationDate = Date.valueOf("2025-05-01");

        // Stub the method getUserRole to return "Student" for a valid request.
        when(userDAOMock.getUserRole(userId)).thenReturn("Student");

        // Stub createReservation to return true.
        when(reservationDAOMock.createReservation(userId, equipmentId, reservationDate))
                .thenReturn(true);

        boolean success = reservationController.requestReservation(userId, equipmentId, reservationDate);
        assertTrue(success, "Reservation should be successful with valid input.");

        // Optional: verify method calls.
        verify(userDAOMock, times(1)).getUserRole(userId);
        verify(reservationDAOMock, times(1)).createReservation(userId, equipmentId, reservationDate);
    }

    @Test
    public void testRequestReservationWithInvalidUser() {
        String userId = "";
        String equipmentId = "E001";
        Date reservationDate = Date.valueOf("2025-05-01");

        boolean success = reservationController.requestReservation(userId, equipmentId, reservationDate);
        assertFalse(success, "Reservation request should fail with invalid user ID.");
    }
}
