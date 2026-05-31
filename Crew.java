// Crew.java
// Abstract base class for airline employees (Pilot, CabinCrew, GroundStaff).
// Crew extends Person directly because crew members do NOT log into the
// passenger system - admin manages them.

import java.util.ArrayList;
import java.util.List;

public abstract class Crew extends Person {

    protected String employeeId;
    protected String role;
    protected String dutySchedule;
    protected boolean isAvailable;
    protected List<Flight> assignedFlights;

    public Crew() { super(); this.assignedFlights = new ArrayList<>(); }

    public Crew(String id, String name, String phoneNumber, String address,
                String employeeId, String role, String dutySchedule) {
       
        super(id, name, phoneNumber, address);
        this.employeeId = employeeId;
        this.role = role;
        this.dutySchedule = dutySchedule;
        this.isAvailable = true;
        this.assignedFlights = new ArrayList<>();
    }

    public void assignFlight(Flight flight) {
        assignedFlights.add(flight);
        isAvailable = false;
    }

    public void updateAvailability(boolean status) {
        this.isAvailable = status;
    }

    public List<Flight> getAssignedFlights() { return assignedFlights; }
    public String getEmployeeId() { return employeeId; }
    public String getRole()       { return role; }
    public boolean isAvailable()  { return isAvailable; }

    public abstract void performDuty();

    @Override
    public String getDetails() {
        return role + " - " + name + " (Emp: " + employeeId + ")" + (isAvailable ? " [Available]" : " [On Duty]");
    }
}
