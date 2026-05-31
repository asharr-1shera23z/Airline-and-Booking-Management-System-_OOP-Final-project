import java.io.Serializable;
// Aircraft.java

public class Aircraft implements Serializable {
    private static final long serialVersionUID = 1L;

    private String aircraftId;
    private String model;
    private int    capacity;
    private String airlineName;
    private String maintenanceStatus;     // "Good" / "Under Maintenance"
    private String availabilityStatus;    // "Available" / "Assigned"

    public Aircraft() { }

    public Aircraft(String aircraftId, String model, int capacity,
                    String airlineName, String maintenanceStatus,
                    String availabilityStatus) {
        this.aircraftId = aircraftId;
        this.model = model;
        this.capacity = capacity;
        this.airlineName = airlineName;
        this.maintenanceStatus = maintenanceStatus;
        this.availabilityStatus = availabilityStatus;
    }

    public void updateStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    // ---------- Getters / Setters ----------
    public String getAircraftId()         { return aircraftId; }
    public String getModel()              { return model; }
    public int    getCapacity()           { return capacity; }
    public String getAirlineName()        { return airlineName; }
    public String getMaintenanceStatus()  { return maintenanceStatus; }
    public String getAvailabilityStatus() { return availabilityStatus; }

    public void setModel(String model)             { this.model = model; }
    public void setCapacity(int capacity)          { this.capacity = capacity; }
    public void setMaintenanceStatus(String s)     { this.maintenanceStatus = s; }
    public void setAvailabilityStatus(String s)    { this.availabilityStatus = s; }

    @Override
    public String toString() {
        return aircraftId + " - " + model + " | Capacity: " + capacity
             + " | Airline: " + airlineName + " | " + availabilityStatus;
    }
}
