// Account.java
// Abstract class that extends Person and adds login-related fields.
// Used as parent for User and Admin (people who actually log in).
// Crew does NOT extend Account because employees don't log in to this system.

public abstract class Account extends Person {

    protected String username;
    protected String password;
    protected String email;
    protected String role;     // "user" or "admin"

    public Account() { super(); }

    public Account(String name, String phoneNumber, String address, String username, String password, String email, String role) {
        super(null, name, phoneNumber, address);
        this.username = username;
        this.password = password;
        this.email    = email;
        this.role     = role;
    }

    // ---------- Common login behaviour ----------
    public boolean login(String username, String password) {
        return this.username != null
            && this.password != null
            && username != null
            && password != null
            && this.username.equals(username)
            && this.password.equals(password);
    }

    public void logout() {
        System.out.println(name + " logged out.");
    }
    //++++++++++++++++++++++++ ERROR +++++++++++++++++++ 
    public void updateProfile(String name, String phoneNumber, String address) {
        this.name        = name;
        this.phoneNumber = phoneNumber;
        this.address     = address;
        System.out.println("Profile updated.");
    }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail()    { return email; }
    public String getRole()     { return role; }

    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email)       { this.email = email; }
}
