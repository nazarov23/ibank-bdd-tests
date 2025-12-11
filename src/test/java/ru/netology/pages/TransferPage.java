package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TransferPage {
    private SelenideElement amountField = $("[data-test-id='amount'] input");
    private SelenideElement fromField = $("[data-test-id='from'] input");
    private SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private SelenideElement cancelButton = $("[data-test-id='action-cancel']");

    public DashboardPage makeTransfer(String fromCardNumber, int amount) {
        enterAmount(amount);
        enterFromCard(fromCardNumber);
        transferButton.click();
        return new DashboardPage();
    }

    public void attemptTransfer(String fromCardNumber, int amount) {
        enterAmount(amount);
        enterFromCard(fromCardNumber);
        transferButton.click();
    }

    public DashboardPage cancelTransfer() {
        cancelButton.click();
        return new DashboardPage();
    }

    public boolean isErrorVisible() {
        return $$("[data-test-id='error-notification'], .notification_error, .error-message")
                .filterBy(Condition.visible).size() > 0;
    }

    public boolean isOnPage() {
        return amountField.isDisplayed() && transferButton.isDisplayed();
    }

    private void enterAmount(int amount) {
        amountField.clear();
        amountField.setValue(String.valueOf(amount));
    }

    private void enterFromCard(String cardNumber) {
        fromField.clear();
        fromField.setValue(cardNumber);
    }
}