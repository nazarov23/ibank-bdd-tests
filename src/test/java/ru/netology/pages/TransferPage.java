package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement pageTitle = $("h2[data-test-id='dashboard']");
    private final SelenideElement transferTitle = $("h1");
    private final SelenideElement amountInput = $("[data-test-id='amount'] input");
    private final SelenideElement fromInput = $("[data-test-id='from'] input");
    private final SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private final SelenideElement cancelButton = $("[data-test-id='action-cancel']");

    public TransferPage() {
        verifyPageIsLoaded();
    }

    private void verifyPageIsLoaded() {
        pageTitle.shouldHave(text("Личный кабинет"));
        transferTitle.shouldHave(exactText("Пополнение карты"));
    }

    // Этот метод должен называться makeValidTransfer (как в тесте)
    public DashboardPage makeValidTransfer(String amount, String fromCardNumber) {
        amountInput.setValue(amount);
        fromInput.setValue(fromCardNumber);
        transferButton.click();
        return new DashboardPage();
    }

    // Или добавьте этот метод, если хотите использовать int
    public DashboardPage makeTransfer(int amount, String fromCardNumber) {
        return makeValidTransfer(String.valueOf(amount), fromCardNumber);
    }

    public DashboardPage cancelTransfer() {
        cancelButton.click();
        return new DashboardPage();
    }
}