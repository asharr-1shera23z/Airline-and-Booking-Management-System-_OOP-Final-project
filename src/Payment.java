import java.io.Serializable;
// Payment.java
// Holds payment record. Uses a PaymentMethod (interface) - this is composition
// + polymorphism: Payment doesn't care which method, it just calls pay().

public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int counter = 1000;        // static counter for ID generation
    private String paymentId;
    private double amount;
    private String status;                     // "Pending" / "Paid" / "Refunded" / "Failed"
    private String paymentDate;
    private PaymentMethod method;

    public Payment(double amount, PaymentMethod method) {
        this.paymentId   = "PAY" + (++counter);
        this.amount      = amount;
        this.method      = method;
        this.status      = "Pending";
        this.paymentDate = java.time.LocalDate.now().toString();
    }

    public boolean processPayment() {
        boolean ok = method.pay(amount);
        status = ok ? "Paid" : "Failed";
        return ok;
    }

    public void refund() {
        if (status.equals("Paid")) {
            status = "Refunded";
            System.out.println("Refund of Rs " + amount + " processed for "
                               + paymentId + ".");
        } else {
            System.out.println("Cannot refund - payment is not in Paid state.");
        }
    }

    public String        getPaymentId()   { return paymentId; }
    public double        getAmount()      { return amount; }
    public String        getStatus()      { return status; }
    public String        getPaymentDate() { return paymentDate; }
    public PaymentMethod getMethod()      { return method; }

    @Override
    public String toString() {
        return paymentId + " | Rs " + amount + " | " + method.getMethodName()
             + " | " + status;
    }
}
