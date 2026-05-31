// ReportManager.java
// Builds reports from the AirlineSystem data.

public class ReportManager {

    private AirlineSystem system;

    public ReportManager(AirlineSystem system) {
        this.system = system;
    }

    public void bookingReport() {
        System.out.println("\n========== BOOKINGS REPORT ==========");
        if (system.getBookings().isEmpty()) { System.out.println("No bookings."); return; }
        int confirmed = 0, cancelled = 0, refunded = 0;
        for (Booking b : system.getBookings()) {
            System.out.println("  " + b.getBookingDetails());
            switch (b.getStatus()) {
                case "Confirmed":            confirmed++; break;
                case "Cancellation Requested": cancelled++; break;
                case "Refunded":
                case "Cancelled by Admin":   refunded++;  break;
            }
        }
        System.out.println("\nTotal bookings: " + system.getBookings().size());
        System.out.println("  Confirmed: " + confirmed
                         + " | Pending Refund: " + cancelled
                         + " | Refunded/Cancelled: " + refunded);
    }

    public void flightReport() {
        System.out.println("\n========== FLIGHTS REPORT ==========");
        if (system.getFlights().isEmpty()) { System.out.println("No flights."); return; }
        for (Flight f : system.getFlights()) {
            System.out.println("  " + f);
            int booked = f.getCapacity() - f.getAvailableSeatsCount();
            double occupancy = f.getCapacity() == 0 ? 0
                            : ((double) booked / f.getCapacity()) * 100;
            System.out.printf("    Booked: %d/%d (%.1f%% occupancy)%n",
                              booked, f.getCapacity(), occupancy);
        }
    }

    public void revenueReport() {
        System.out.println("\n========== REVENUE REPORT ==========");
        double total = 0, refunded = 0;
        for (Booking b : system.getBookings()) {
            if (b.getStatus().equals("Confirmed")) {
                total += b.getTotalAmount();
            } else if (b.getStatus().equals("Refunded")
                    || b.getStatus().equals("Cancelled by Admin")) {
                refunded += b.getTotalAmount();
            }
        }
        System.out.println("  Total earned (confirmed): Rs " + total);
        System.out.println("  Refunded amount        : Rs " + refunded);
        System.out.println("  Net revenue            : Rs " + (total - refunded));
    }

    public void refundReport() {
        System.out.println("\n========== REFUNDS REPORT ==========");
        if (system.getRefundRequests().isEmpty()) {
            System.out.println("No refund requests."); return;
        }
        int p = 0, a = 0, r = 0;
        for (RefundRequest req : system.getRefundRequests()) {
            System.out.println("  " + req);
            switch (req.getStatus()) {
                case "Pending":  p++; break;
                case "Approved": a++; break;
                case "Rejected": r++; break;
            }
        }
        System.out.println("\nPending: " + p + " | Approved: " + a + " | Rejected: " + r);
    }

    public void fullReport() {
        System.out.println("\n############ FULL SYSTEM REPORT ############");
        System.out.println("Users      : " + system.getUsers().size());
        System.out.println("Airports   : " + system.getAirports().size());
        System.out.println("Aircrafts  : " + system.getAircrafts().size());
        System.out.println("Flights    : " + system.getFlights().size());
        System.out.println("Crew       : " + system.getCrewMembers().size());
        bookingReport();
        flightReport();
        revenueReport();
        refundReport();
    }
}
