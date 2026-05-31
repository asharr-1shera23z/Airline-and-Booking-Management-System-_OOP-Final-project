// FileHandler.java
// Generic file handling — saves and loads ArrayList objects using
// Java's built-in Object Serialization (no third-party libraries needed).

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String DATA_DIR = "data";
    static {
        // Create data directory once at class loading time
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    // Save any list to file
    @SuppressWarnings("unchecked")
    public static <T> void saveList(List<T> list, String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(DATA_DIR + "/" + fileName))) {
            out.writeObject(new ArrayList<>(list));
        } catch (IOException e) {
            System.out.println("Error saving " + fileName + ": " + e.getMessage());
        }
    }

    // Load any list from file
    @SuppressWarnings("unchecked")
    public static <T> List<T> loadList(String fileName) {
        File f = new File(DATA_DIR + "/" + fileName);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return (List<T>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading " + fileName + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Save a single object (for Admin which is just one)
    public static void saveObject(Object obj, String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(DATA_DIR + "/" + fileName))) {
            out.writeObject(obj);
        } catch (IOException e) {
            System.out.println("Error saving " + fileName + ": " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T loadObject(String fileName) {
        File f = new File(DATA_DIR + "/" + fileName);
        if (!f.exists()) return null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return (T) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading " + fileName + ": " + e.getMessage());
            return null;
        }
    }

    // Save the whole AirlineSystem state in one shot
    public static void saveSystem(AirlineSystem system) {
        saveList(system.getUsers(),         "users.dat");
        if (system.getAdmin() != null) saveObject(system.getAdmin(), "admin.dat");
        saveList(system.getAirports(),      "airports.dat");
        saveList(system.getAircrafts(),     "aircrafts.dat");
        saveList(system.getFlights(),       "flights.dat");
        saveList(system.getCrewMembers(),   "crew.dat");
        saveList(system.getBookings(),      "bookings.dat");
        saveList(system.getRefundRequests(), "refunds.dat");
        System.out.println(">> Data saved to files.");
    }

    // Load all lists into the AirlineSystem
    @SuppressWarnings("unchecked")
    public static void loadSystem(AirlineSystem system) {
        List<User>          users    = FileHandler.<User>loadList("users.dat");
        List<Airport>       airports = FileHandler.<Airport>loadList("airports.dat");
        List<Aircraft>      aircrafts = FileHandler.<Aircraft>loadList("aircrafts.dat");
        List<Flight>        flights  = FileHandler.<Flight>loadList("flights.dat");
        List<Crew>          crew     = FileHandler.<Crew>loadList("crew.dat");
        List<Booking>       bookings = FileHandler.<Booking>loadList("bookings.dat");
        List<RefundRequest> refunds  = FileHandler.<RefundRequest>loadList("refunds.dat");
        Admin admin = loadObject("admin.dat");

        for (User u : users)        system.addUser(u);
        for (Airport a : airports)  system.addAirport(a);
        for (Aircraft a : aircrafts) system.addAircraft(a);
        for (Flight f : flights)    system.addFlight(f);
        for (Crew c : crew)         system.addCrew(c);
        for (Booking b : bookings)  system.addBooking(b);
        for (RefundRequest r : refunds) system.addRefundRequest(r);
        if (admin != null) system.setAdmin(admin);

        if (!users.isEmpty() || !airports.isEmpty()) {
            System.out.println(">> Data loaded from files.");
        }
    }
}
