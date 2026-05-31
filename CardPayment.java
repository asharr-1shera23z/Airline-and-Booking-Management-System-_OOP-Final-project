// CardPayment.java

public class CardPayment implements PaymentMethod {

    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;

    public CardPayment(String cardNumber, String cardHolderName, String expiryDate) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean pay(double amount) {
        // Simple validation - card number should be 16 digits
        if (cardNumber == null || cardNumber.length() < 12) {
            System.out.println("Card payment failed: invalid card number.");
            return false;
        }
        System.out.println("Card payment of Rs " + amount + " successful for "
                           + cardHolderName + ".");
        return true;
    }

    @Override
    public String getMethodName() { return "Card"; }
}
