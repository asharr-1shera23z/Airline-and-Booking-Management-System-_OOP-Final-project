// CabinCrew.java

public class CabinCrew extends Crew {

    private String sectionAssigned;

    public CabinCrew() { super(); }

    public CabinCrew(String id, String name, String phoneNumber, String address,
                     String employeeId, String dutySchedule,
                     String sectionAssigned) {
        super(id, name, phoneNumber, address, employeeId, "CabinCrew", dutySchedule);
        this.sectionAssigned = sectionAssigned;
    }

    @Override
    public void performDuty() {
        System.out.println("CabinCrew " + name + " is assisting passengers in "
                           + sectionAssigned + " section.");
    }

    public String getSectionAssigned() { return sectionAssigned; }
}
