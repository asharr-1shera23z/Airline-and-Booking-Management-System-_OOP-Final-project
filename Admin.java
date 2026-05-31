// Admin.java
// The single airline admin. Manages flights, airports, aircrafts, crew,
// views bookings, approves refunds, and generates reports.

public class Admin extends Account {

    private String adminKey;     // secret key required at signup

    public Admin() { super(); }

    public Admin( String name, String phoneNumber, String address,
                 String username, String password, String email,
                 String adminKey) {
        super( name, phoneNumber, address, username, password, email, "admin");
        this.adminKey = adminKey;
    }

    public String getAdminKey() { return adminKey; }

    @Override
    public String getDetails() {
        return "Admin: " + name + " | Email: " + email;
    }
}
