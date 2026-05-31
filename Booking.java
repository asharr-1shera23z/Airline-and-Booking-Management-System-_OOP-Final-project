import java.io.Serializable;
// Booking.java
// Connects User, Flight, Seat, Payment, and Ticket together.
// Booking COMPOSES Payment and Ticket (they don't exist without booking).

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int counter = 100;
    private String bookingId;
    private User user;
    private Flight flight;
    private Seat seat;
    private String bookingDate;
    private String status;          // "Confirmed" / "Cancellation Requested" / "Refunded" / "Cancelled by Admin"
    private Payment payment;
    private Ticket  ticket;
    private double  luggageWeight;
    private double  totalAmount;

    public Booking(User user, Flight flight, Seat seat, double luggageWeight) {
        this.bookingId    = "BK" + (++counter);
        this.user         = user;
        this.flight       = flight;
        this.seat         = seat;
        this.luggageWeight = luggageWeight;
        this.bookingDate  = java.time.LocalDate.now().toString();
        this.status       = "Pending";
    }

    public void confirmBooking() {
        seat.reserveSeat();
        ticket = new Ticket(this);
        status = "Confirmed";
    }

    public void cancelBooking() {
        // user-driven cancellation → goes to refund queue
        status = "Cancellation Requested";
    }

    public void requestRefund() {
        // wrapper for clarity - same as cancelBooking in our flow
        cancelBooking();
    }

    public String getBookingDetails() {
        return "Booking " + bookingId
             + " | User: " + user.getName()
             + " | Flight: " + flight.getFlightId()
             + " | Seat: "   + seat.getSeatNumber()
             + " | Amount: Rs " + totalAmount
             + " | Status: " + status;
    }

    // ---------- Getters / Setters ----------
    public String  getBookingId()     { return bookingId; }
    public User    getUser()          { return user; }
    public Flight  getFlight()        { return flight; }
    public Seat    getSeat()          { return seat; }
    public String  getBookingDate()   { return bookingDate; }
    public String  getStatus()        { return status; }
    public Payment getPayment()       { return payment; }
    public Ticket  getTicket()        { return ticket; }
    public double  getLuggageWeight() { return luggageWeight; }
    public double  getTotalAmount()   { return totalAmount; }

    public void setStatus(String status)         { this.status = status; }
    public void setPayment(Payment p)            { this.payment = p; }
    public void setTotalAmount(double total)     { this.totalAmount = total; }
}
