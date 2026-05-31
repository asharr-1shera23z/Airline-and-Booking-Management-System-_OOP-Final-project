public interface PaymentMethod extends java.io.Serializable {
    boolean pay(double amount);
    String  getMethodName();
}
