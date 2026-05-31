import java.io.Serializable;
// Seat.java

public class Seat implements Serializable {
    private static final long serialVersionUID = 1L;

    private int seatNumber;
    private boolean isBooked;
    private String classType;     // Economy / Business / First

    public Seat() { }

    public Seat(int seatNumber, String classType) {
        this.seatNumber = seatNumber;
        this.classType  = classType;
        this.isBooked   = false;
    }

    public void reserveSeat() { this.isBooked = true;  }
    public void releaseSeat() { this.isBooked = false; }

    public int     getSeatNumber() { return seatNumber; }
    public boolean isBooked()      { return isBooked; }
    public String  getClassType()  { return classType; }

    @Override
    public String toString() {
        return "Seat " + seatNumber + " (" + classType + ")"
             + (isBooked ? " [BOOKED]" : " [Available]");
    }
}
