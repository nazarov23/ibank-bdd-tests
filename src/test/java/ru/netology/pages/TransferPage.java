package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {

    private SelenideElement amountField = $("[data-test-id=amount] input");
    private SelenideElement fromField = $("[data-test-id=from] input");
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private SelenideElement cancelButton = $("[data-test-id=action-cancel]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification]");
    private SelenideElement pageHeading = $("h1");

    public TransferPage() {
        // Конструктор проверяет состояние элементов, но ничего не возвращает
        verifyPageIsLoaded();
    }

    // Приватный метод для проверки состояния страницы
    private void verifyPageIsLoaded() {
        pageHeading.shouldBe(visible).shouldHave(exactText("Пополнение карты"));
        amountField.shouldBe(visible);
        fromField.shouldBe(visible);
        transferButton.shouldBe(enabled);
        cancelButton.shouldBe(enabled);
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

    public void shouldShowError() {
        // Метод проверяет видимость ошибки, но не возвращает boolean
        errorNotification.shouldBe(visible);
    }

    // Дополнительный метод для проверки текста ошибки (опционально)
    public void shouldShowErrorWithText(String expectedText) {
        errorNotification.shouldBe(visible).shouldHave(text(expectedText));
    }
}