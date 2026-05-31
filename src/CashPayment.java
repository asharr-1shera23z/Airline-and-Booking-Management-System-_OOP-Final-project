// CashPayment.java

public class CashPayment implements PaymentMethod {

    @Override
    public boolean pay(double amount) {
        System.out.println("Cash payment of Rs " + amount + " collected at counter.");
        return true;
    }

    @Override
    public String getMethodName() { return "Cash"; }
}
