import java.io.Serializable;
// Ticket.java

public class Ticket implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int counter = 5000;
    private String ticketId;
    private Booking booking;
    private String  issueDate;
    private String  details;

    public Ticket(Booking booking) {
        this.ticketId  = "TKT" + (++counter);
        this.booking   = booking;
        this.issueDate = java.time.LocalDate.now().toString();
        this.details   = generateDetails();
    }

    private String generateDetails() {
        Flight f = booking.getFlight();
        return "Passenger: " + booking.getUser().getName()
             + " | Flight: " + f.getFlightId()
             + " | Route: " + f.getSource().getCity() + " -> " + f.getDestination().getCity()
             + " | Seat: "   + booking.getSeat().getSeatNumber()
             + " (" + booking.getSeat().getClassType() + ")";
    }

    public String viewTicket() {
        return "___________Ticket___________"
             + "Ticket ID : " + ticketId   + "\n"
             + "Issued    : " + issueDate  + "\n"
             + details + "\n"
             + "Status    : " + booking.getStatus();         
    }

    public String getTicketId() { return ticketId; }
    public Booking getBooking() { return booking; }
    public String  getDetails() { return details; }
}
