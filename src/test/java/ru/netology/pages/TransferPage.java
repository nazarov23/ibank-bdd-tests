package ru.netology.pages;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class TransferPage {

    public TransferPage() {
        // Verify we're on transfer page
        $("h1").shouldHave(text("Пополнение карты").or(text("Перевод")));
        $("[data-test-id='amount']").shouldBe(visible);
        $("[data-test-id='from']").shouldBe(visible);
    }

    public DashboardPage makeValidTransfer(String amount, String fromCard) {
        $("[data-test-id='amount'] input").setValue(amount);
        $("[data-test-id='from'] input").setValue(fromCard);
        $("[data-test-id='action-transfer']").click();

        return new DashboardPage();
    }

    public DashboardPage cancelTransfer() {
        $("[data-test-id='action-cancel']").click();
        return new DashboardPage();
    }
}