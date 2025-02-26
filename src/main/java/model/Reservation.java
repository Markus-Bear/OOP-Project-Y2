package model;

import java.util.Date;

public class Reservation {
    private int reservationId;
    private String userId;
    private String equipmentId;
    private Date reservationDate;
    private Date returnDate;
    private String status;  // Pending, Approved, Rejected

    // Constructor
    public Reservation(int reservationId, String userId, String equipmentId, Date reservationDate, Date returnDate, String status) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.equipmentId = equipmentId;
        this.reservationDate = reservationDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public Reservation() {
        this.reservationId = 0;
        this.userId = "";
        this.equipmentId = "";
        this.reservationDate = null;
        this.returnDate = null;
        this.status = "";
    }

    // Getters and Setters
    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEquipmentId() { return equipmentId; }
    public void setEquipmentId(String equipmentId) { this.equipmentId = equipmentId; }

    public Date getReservationDate() { return reservationDate; }
    public void setReservationDate(Date reservationDate) { this.reservationDate = reservationDate; }

    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
