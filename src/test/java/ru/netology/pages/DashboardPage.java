package ru.netology.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {
    private final SelenideElement heading = $("[data-test-id='dashboard']");
    private final ElementsCollection cards = $$(".list__item");

    public void shouldBeVisible() {
        heading.shouldBe(visible);
    }

    public int getCardBalance(String cardId) {
        SelenideElement card = $("[data-test-id='" + cardId + "']");
        String text = card.getText();
        return extractBalance(text);
    }

    public int getFirstCardBalance() {
        return getCardBalance("92df3f1c-a033-48e6-8390-206f6b1f56c0");
    }

    public int getSecondCardBalance() {
        return getCardBalance("0f3f5c2a-249e-4c3d-8287-09f7a039391d");
    }

    private int extractBalance(String text) {
        String balanceStart = "баланс: ";
        String balanceFinish = " р.";

        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);

        String value = text.substring(start + balanceStart.length(), finish).trim();
        return Integer.parseInt(value);
    }

    public TransferPage transferTo(String cardId) {
        $("[data-test-id='" + cardId + "'] [data-test-id='action-deposit']").click();
        return new TransferPage();
    }

    public TransferPage transferToFirstCard() {
        return transferTo("92df3f1c-a033-48e6-8390-206f6b1f56c0");
    }

    public TransferPage transferToSecondCard() {
        return transferTo("0f3f5c2a-249e-4c3d-8287-09f7a039391d");
    }
}