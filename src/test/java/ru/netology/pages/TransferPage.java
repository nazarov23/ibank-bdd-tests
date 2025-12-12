package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper.CardInfo;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement amountField = $("[data-test-id='amount'] input");
    private final SelenideElement fromField = $("[data-test-id='from'] input");
    private final SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification']");
    private final SelenideElement cancelButton = $("[data-test-id='action-cancel']");

    public DashboardPage makeValidTransfer(String amount, CardInfo fromCard) {
        fillTransferForm(amount, fromCard);
        transferButton.click();
        return new DashboardPage();
    }

    public TransferPage makeInvalidTransfer(String amount, CardInfo fromCard) {
        fillTransferForm(amount, fromCard);
        transferButton.click();
        return this;
    }

    public DashboardPage cancelTransfer() {
        cancelButton.click();
        return new DashboardPage();
    }

    private void fillTransferForm(String amount, CardInfo fromCard) {
        amountField.setValue(amount);
        fromField.setValue(fromCard.getCardNumber());
    }

    public void shouldHaveError(String expectedError) {
        errorNotification.shouldBe(visible)
                .shouldHave(text(expectedError));
    }

    public void shouldBeVisible() {
        amountField.shouldBe(visible);
    }
}