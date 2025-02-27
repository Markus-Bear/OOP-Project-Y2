package model;

import java.util.Date;

/**
 * Represents a reservation for a piece of equipment.
 * Contains details such as reservation ID, user ID, equipment ID, reservation date, return date, and status.
 */
public class Reservation {
    private int reservationId;
    private String userId;
    private String equipmentId;
    private Date reservationDate;
    private Date returnDate;
    private String status;  // Pending, Approved, Rejected

    /**
     * Constructs a Reservation with the specified details.
     *
     * @param reservationId   the unique reservation ID
     * @param userId          the ID of the user making the reservation
     * @param equipmentId     the ID of the equipment being reserved
     * @param reservationDate the date of the reservation
     * @param returnDate      the expected return date of the equipment
     * @param status          the status of the reservation (e.g., Pending, Approved, Rejected)
     */
    public Reservation(int reservationId, String userId, String equipmentId, Date reservationDate, Date returnDate, String status) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.equipmentId = equipmentId;
        this.reservationDate = reservationDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    /**
     * Constructs an empty Reservation with default values.
     */
    public Reservation() {
        this.reservationId = 0;
        this.userId = "";
        this.equipmentId = "";
        this.reservationDate = null;
        this.returnDate = null;
        this.status = "";
    }

    /**
     * Returns the reservation ID.
     *
     * @return the reservation ID.
     */
    public int getReservationId() {
        return reservationId;
    }

    /**
     * Sets the reservation ID.
     *
     * @param reservationId the reservation ID to set.
     */
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    /**
     * Returns the user ID of the user who made the reservation.
     *
     * @return the user ID.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user ID for this reservation.
     *
     * @param userId the user ID to set.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Returns the equipment ID of the reserved equipment.
     *
     * @return the equipment ID.
     */
    public String getEquipmentId() {
        return equipmentId;
    }

    /**
     * Sets the equipment ID for this reservation.
     *
     * @param equipmentId the equipment ID to set.
     */
    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    /**
     * Returns the reservation date.
     *
     * @return the reservation date.
     */
    public Date getReservationDate() {
        return reservationDate;
    }

    /**
     * Sets the reservation date.
     *
     * @param reservationDate the reservation date to set.
     */
    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    /**
     * Returns the return date.
     *
     * @return the return date.
     */
    public Date getReturnDate() {
        return returnDate;
    }

    /**
     * Sets the return date.
     *
     * @param returnDate the return date to set.
     */
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * Returns the status of the reservation.
     *
     * @return the reservation status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the reservation.
     *
     * @param status the status to set (e.g., Pending, Approved, Rejected).
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
