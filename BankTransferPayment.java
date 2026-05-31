// BankTransferPayment.java

public class BankTransferPayment implements PaymentMethod {

    private String accountNumber;
    private String bankName;

    public BankTransferPayment(String accountNumber, String bankName) {
        this.accountNumber = accountNumber;
        this.bankName = bankName;
    }

    @Override
    public boolean pay(double amount) {
        if (accountNumber == null || accountNumber.isEmpty()) {
            System.out.println("Bank transfer failed: invalid account.");
            return false;
        }
        System.out.println("Bank transfer of Rs " + amount + " from "
                           + bankName + " account " + accountNumber + " successful.");
        return true;
    }

    @Override
    public String getMethodName() { return "Bank Transfer"; }
}
