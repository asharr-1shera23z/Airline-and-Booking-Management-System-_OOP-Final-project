// Pilot.java

public class Pilot extends Crew {

    private String licenseNumber;
    private int experienceYears;

    public Pilot() { super(); }

    public Pilot(String id, String name, String phoneNumber, String address,
                 String employeeId, String dutySchedule,
                 String licenseNumber, int experienceYears) 
                 {
        super(id, name, phoneNumber, address, employeeId, "Pilot", dutySchedule);
        this.licenseNumber  = licenseNumber;
        this.experienceYears = experienceYears;
    }

    @Override
    public void performDuty() {
        System.out.println("Pilot " + name + " is flying the aircraft.");
    }

    public String getLicenseNumber() { return licenseNumber; }
    public int getExperienceYears()  { return experienceYears; }
}
