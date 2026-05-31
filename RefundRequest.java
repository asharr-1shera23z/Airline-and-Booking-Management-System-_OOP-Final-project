import java.io.Serializable;
// RefundRequest.java

public class RefundRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int counter = 0;
    private String  requestId;
    private Booking booking;
    private String  reason;
    private String  status;          // "Pending" / "Approved" / "Rejected"
    private String  requestDate;

    public RefundRequest(Booking booking, String reason) {
        this.requestId   = "REF" + (++counter);
        this.booking     = booking;
        this.reason      = reason;
        this.status      = "Pending";
        this.requestDate = java.time.LocalDate.now().toString();
    }

    public void approve() { this.status = "Approved"; }
    public void reject()  { this.status = "Rejected"; }

    public String  getRequestId()   { return requestId; }
    public Booking getBooking()     { return booking; }
    public String  getReason()      { return reason; }
    public String  getStatus()      { return status; }
    public String  getRequestDate() { return requestDate; }

    @Override
    public String toString() {
        return requestId + " | Booking: " + booking.getBookingId()
             + " | User: " + booking.getUser().getName()
             + " | Reason: " + reason
             + " | Status: " + status;
    }
}
