package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class TransferPage {
    private final SelenideElement amountField = $("[data-test-id='amount'] input");
    private final SelenideElement fromField = $("[data-test-id='from'] input");
    private final SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private final SelenideElement cancelButton = $("[data-test-id='action-cancel']");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification']");

    public DashboardPage makeTransfer(String amount, DataHelper.CardInfo fromCard) {
        amountField.setValue(amount);
        fromField.setValue(fromCard.getCardNumber());
        transferButton.click();
        return new DashboardPage();
    }

    public void makeInvalidTransfer(String amount, DataHelper.CardInfo fromCard) {
        amountField.setValue(amount);
        fromField.setValue(fromCard.getCardNumber());
        transferButton.click();
    }

    public void shouldHaveError() {
        errorNotification.shouldBe(visible);
    }
}