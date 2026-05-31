import java.io.Serializable;
// Flight.java
// Central class. Contains seats (composition) and is associated with
// an Aircraft, two Airports (source/destination), and a list of Crew.

import java.util.ArrayList;
import java.util.List;

public class Flight implements Serializable {
    private static final long serialVersionUID = 1L;

    private String flightId;
    private Airport source;
    private Airport destination;
    private String  departureTime;
    private String  arrivalTime;
    private double  fare;
    private int     capacity;
    private String  status;         // "Scheduled" / "Cancelled" / "Completed"
    private Aircraft aircraft;
    private List<Seat> seats;
    private List<Crew> crew;

    public Flight() {
        this.seats = new ArrayList<>();
        this.crew  = new ArrayList<>();
    }

    public Flight(String flightId, Airport source, Airport destination,
                  String departureTime, String arrivalTime, double fare,
                  Aircraft aircraft) {
        this.flightId = flightId;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.fare = fare;
        this.aircraft = aircraft;
        this.capacity = aircraft.getCapacity();
        this.status = "Scheduled";
        this.seats  = new ArrayList<>();
        this.crew   = new ArrayList<>();
        generateSeats();
    }

    // Auto-generate seats based on aircraft capacity
    private void generateSeats() {
        int total = capacity;
        int firstClass  = Math.max(1, total / 10);
        int business    = Math.max(2, total / 5);
        int economy     = total - firstClass - business;

        int n = 1;
        for (int i = 0; i < firstClass; i++) seats.add(new Seat(n++, "First"));
        for (int i = 0; i < business;   i++) seats.add(new Seat(n++, "Business"));
        for (int i = 0; i < economy;    i++) seats.add(new Seat(n++, "Economy"));
    }

    public List<Seat> getAvailableSeats() {
        List<Seat> available = new ArrayList<>();
        for (Seat s : seats) {
            if (!s.isBooked()) available.add(s);
        }
        return available;
    }

    public int getAvailableSeatsCount() { return getAvailableSeats().size(); }

    public void assignCrew(Crew c)  { crew.add(c); }
    public void updateStatus(String status) { this.status = status; }

    // ---------- Getters / Setters ----------
    public String   getFlightId()      { return flightId; }
    public Airport  getSource()        { return source; }
    public Airport  getDestination()   { return destination; }
    public String   getDepartureTime() { return departureTime; }
    public String   getArrivalTime()   { return arrivalTime; }
    public double   getFare()          { return fare; }
    public int      getCapacity()      { return capacity; }
    public String   getStatus()        { return status; }
    public Aircraft getAircraft()      { return aircraft; }
    public List<Seat> getSeats()       { return seats; }
    public List<Crew> getCrew()        { return crew; }

    public void setFare(double fare)               { this.fare = fare; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

    @Override
    public String toString() {
        return flightId + " | " + source.getCity() + " -> " + destination.getCity()
             + " | Dep: " + departureTime + " | Fare: Rs " + fare
             + " | Available: " + getAvailableSeatsCount() + "/" + capacity
             + " | Status: " + status;
    }
}
