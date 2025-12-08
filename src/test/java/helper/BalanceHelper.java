package helper;

public class BalanceHelper {
    public static int calculateExpectedBalance(int currentBalance, int transferAmount) {
        return currentBalance + transferAmount;
    }
}