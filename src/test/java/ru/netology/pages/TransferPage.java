package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {

    private SelenideElement amountField = $("[data-test-id=amount] input");
    private SelenideElement fromField = $("[data-test-id=from] input");
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private SelenideElement cancelButton = $("[data-test-id=action-cancel]");

    public TransferPage() {
        $("h1").shouldBe(visible).shouldHave(text("Пополнение карты"));
    }

    public void makeTransfer(String amount, String fromCard) {
        amountField.setValue(amount);
        fromField.setValue(fromCard);
        transferButton.click();
    }

    public DashboardPage makeValidTransfer(String amount, String fromCard) {
        makeTransfer(amount, fromCard);
        return new DashboardPage();
    }

    public DashboardPage cancelTransfer() {
        cancelButton.click();
        return new DashboardPage();
    }

    public boolean shouldShowError() {
        $("[data-test-id=error-notification]").shouldBe(visible);
        return $("[data-test-id=error-notification]").is(visible);
    }
}