// AirlineSystem.java
// Central container - holds all lists. This is the "single source of truth"
// for the program. User and Admin both work on the same lists.

import java.util.ArrayList;
import java.util.List;

public class AirlineSystem {

    private List<User>          users;
    private Admin               admin;            // only one admin
    private List<Flight>        flights;
    private List<Airport>       airports;
    private List<Aircraft>      aircrafts;
    private List<Crew>          crewMembers;
    private List<Booking>       bookings;
    private List<RefundRequest> refundRequests;
    private List<Notification>  notifications;

    public AirlineSystem() {
        this.users          = new ArrayList<>();
        this.flights        = new ArrayList<>();
        this.airports       = new ArrayList<>();
        this.aircrafts      = new ArrayList<>();
        this.crewMembers    = new ArrayList<>();
        this.bookings       = new ArrayList<>();
        this.refundRequests = new ArrayList<>();
        this.notifications  = new ArrayList<>();
    }

    // ---------- User ----------
    public void addUser(User u) { users.add(u); }
    public List<User> getUsers() { return users; }

    // ---------- Admin ----------
    public void  setAdmin(Admin a) { this.admin = a; }
    public Admin getAdmin()        { return admin; }

    // ---------- Airports ----------

    public void addAirport(Airport a) { airports.add(a); }

    public Airport findAirport(String code) {

            for (Airport a : airports) {
                 if (a.getAirportCode().equalsIgnoreCase(code)) { return a;}
            }
            return null;
        
    }
    public boolean isAirportInUse(String code) {
        for (Flight f : flights) {
            if (f.getSource().getAirportCode().equalsIgnoreCase(code)
             || f.getDestination().getAirportCode().equalsIgnoreCase(code)) return true;
        }
        return false;
    }
    public void removeAirport(String code) {
        airports.removeIf(a -> a.getAirportCode().equalsIgnoreCase(code));
    }
    public List<Airport> getAirports() { return airports; }

    
    // ---------- Aircrafts ----------
    public void addAircraft(Aircraft ac) { aircrafts.add(ac); }
    public Aircraft findAircraft(String id) {
        for (Aircraft a : aircrafts) if (a.getAircraftId().equalsIgnoreCase(id)) return a;
        return null;
    }
    public List<Aircraft> getAircrafts() { return aircrafts; }

    public void removeAircraft(String id) {
        if (id == null || id.trim().isEmpty()) {
            return;
        }
        aircrafts.removeIf(a -> a.getAircraftId() != null && a.getAircraftId().equalsIgnoreCase(id));
    }

    // ---------- Flights ----------
    public void addFlight(Flight f) { flights.add(f); }
    public Flight findFlight(String id) {
        for (Flight f : flights) if (f.getFlightId().equalsIgnoreCase(id)) return f;
        return null;
    }
    public void removeFlight(String id) {
        flights.removeIf(f -> f.getFlightId().equalsIgnoreCase(id));
    }
    public List<Flight> getFlights() { return flights; }

    // Search flights — overloaded versions (Method Overloading)
    public List<Flight> searchFlights() {
        List<Flight> r = new ArrayList<>();
        for (Flight f : flights) if (f.getStatus().equalsIgnoreCase("Scheduled")) r.add(f);
        return r;
    }

    public List<Flight> searchFlights(String sourceCity, String destCity) {
        List<Flight> r = new ArrayList<>();
        for (Flight f : flights) {
            if (f.getStatus().equalsIgnoreCase("Scheduled")
             && f.getSource().getCity().equalsIgnoreCase(sourceCity)
             && f.getDestination().getCity().equalsIgnoreCase(destCity)) {
                r.add(f);
            }
        }
        return r;
    }

    // ---------- Crew ----------
    public void addCrew(Crew c) { crewMembers.add(c); }
    public List<Crew> getCrewMembers() { return crewMembers; }
    public void removeCrew(String empId) {
        crewMembers.removeIf(c ->
            c != null &&
            c.getEmployeeId() != null &&
            c.getEmployeeId().equalsIgnoreCase(empId)
        );
    }

    // ---------- Bookings ----------
    public void addBooking(Booking b) { bookings.add(b); }
    public Booking findBooking(String id) {
        for (Booking b : bookings) if (b.getBookingId().equalsIgnoreCase(id)) return b;
        return null;
    }
    public List<Booking> getBookings() { return bookings; }

    // ---------- Refund requests ----------
    public void addRefundRequest(RefundRequest r) { refundRequests.add(r); }
    public List<RefundRequest> getPendingRefunds() {
        List<RefundRequest> r = new ArrayList<>();
        for (RefundRequest rr : refundRequests) if (rr.getStatus().equals("Pending")) r.add(rr);
        return r;
    }
    public List<RefundRequest> getRefundRequests() { return refundRequests; }
    public RefundRequest getRefundRequest(String id) {
        for (RefundRequest r : refundRequests) if (r.getRequestId().equalsIgnoreCase(id)) return r;
        return null;
    }
    // ---------- Notifications ----------
    public void addNotification(Notification n) { notifications.add(n); }
    public List<Notification> getNotificationsFor(User user) {
        List<Notification> r = new ArrayList<>();
        for (Notification n : notifications) if (n.getRecipient() == user) r.add(n);
        return r;
    }
}
