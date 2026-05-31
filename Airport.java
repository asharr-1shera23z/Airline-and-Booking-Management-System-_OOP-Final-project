import java.io.Serializable;
// Airport.java

public class Airport implements Serializable {
    private static final long serialVersionUID = 1L;

    private String airportCode;
    private String name;
    private String city;
    private String country;
    private String status;       // "Active" or "Inactive"

    public Airport() { }

    public Airport(String airportCode, String name, String city,
                   String country, String status) {
        this.airportCode = airportCode;
        this.name = name;
        this.city = city;
        this.country = country;
        this.status = status;
    }

    // ---------- Getters / Setters ----------
    public String getAirportCode() { return airportCode; }
    public String getName()        { return name; }
    public String getCity()        { return city; }
    public String getCountry()     { return country; }
    public String getStatus()      { return status; }

    public void setName(String name)       { this.name = name; }
    public void setCity(String city)       { this.city = city; }
    public void setCountry(String country) { this.country = country; }
    public void setStatus(String status)   { this.status = status; }

    @Override
    public String toString() {
        return airportCode + " - " + name + " (" + city + ", " + country
             + ") [" + status + "]";
    }
}
