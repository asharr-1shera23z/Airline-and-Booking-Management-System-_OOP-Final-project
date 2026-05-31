// User.java
// A customer who can search flights, book tickets, view their bookings,
// and request cancellation/refund. Inherits from Account.

import java.util.ArrayList;
import java.util.List;

public class User extends Account {

    private String cnic;
    private String city;
    private List<Booking> bookings;

    public User() {
        super();
        this.bookings = new ArrayList<>();
    }

    public User( String name, String phoneNumber, String address, String username, String password, String email,
                String cnic, String city) {
       
        super( name, phoneNumber, address, username, password, email, "user");
        this.cnic = cnic;
        this.city = city;
        this.bookings = new ArrayList<>();
    }

    // ---------- User-specific behaviour ----------

    // Calculate fare = base fare + extra luggage charge (overloaded helper)
    public double calculateFare(Flight flight, double luggageWeight) {
        double base = flight.getFare();
        double extra = 0;
        double allowed = 20.0;                    // 20 kg free
        if (luggageWeight > allowed) {
            extra = (luggageWeight - allowed) * 800;   // Rs 800 per extra kg
        }
        return base + extra;
    }

    public void addBooking(Booking b) { bookings.add(b); }

    public List<Booking> getBookings() { return bookings; }
    public String getCnic() { return cnic; }
    public String getCity() { return city; }

    @Override
    public String getDetails() {
        return "User: " + name + " | CNIC: " + cnic + " | City: " + city
             + " | Email: " + email;
    }
}
