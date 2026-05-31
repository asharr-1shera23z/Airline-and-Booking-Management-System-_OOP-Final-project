// CardPayment.java

public class CardPayment implements PaymentMethod {

    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;

    public CardPayment(String cardNumber, String cardHolderName, String expiryDate, String cvv) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    @Override
    public boolean pay(double amount) {
        if (cardNumber == null || !cardNumber.matches("^\\d{16}$")) {
            System.out.println("Card payment failed: Card number must be exactly 16 digits.");
            return false;
        }

        if (cardHolderName == null || !cardHolderName.matches("^[a-zA-Z\\s]+$")) {
            System.out.println("Card payment failed: Name must contain only alphabetic characters.");
            return false;
        }

        if (expiryDate == null || !expiryDate.matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
            System.out.println("Card payment failed: Expiry must follow the MM/YY format.");
            return false;
        }

        if (cvv == null || !cvv.matches("^\\d{3,4}$")) {
            System.out.println("Card payment failed: CVV must be exactly 3 or 4 digits.");
            return false;
        }

        System.out.println("Card payment of Rs " + amount + " successful for " + cardHolderName + ".");
        return true;
    }

    @Override
    public String getMethodName() { return "Card"; }
}
