// GroundStaff.java

public class GroundStaff extends Crew {

    private String department;

    public GroundStaff() { super(); }

    public GroundStaff(String id, String name, String phoneNumber, String address,
                       String employeeId, String dutySchedule,
                       String department) {
        super(id, name, phoneNumber, address, employeeId, "GroundStaff", dutySchedule);
        this.department = department;
    }

    @Override
    public void performDuty() {
        System.out.println("GroundStaff " + name + " is handling "
                           + department + " duties.");
    }

    public String getDepartment() { return department; }
}
