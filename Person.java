// Person.java
// Abstract base class for any human in the system (User, Admin, Crew).
// Holds the universal human attributes that every person has.

import java.io.Serializable;

public abstract class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String id;
    protected String name;
    protected String phoneNumber;
    protected String address;

    // No-arg constructor (needed for some flows)
    public Person() { }

    // Parameterized constructor
    public Person(String id, String name, String phoneNumber, String address) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    // ---------- Getters / Setters (Encapsulation) ----------
    public String getId()          { return id; }
    public String getName()        { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAddress()     { return address; }

    public void setName(String name)               { this.name = name; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setAddress(String address)         { this.address = address; }

    public abstract String getDetails() ;

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Phone: " + phoneNumber;
    }
}
