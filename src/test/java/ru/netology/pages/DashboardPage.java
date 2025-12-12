package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
    private final SelenideElement heading = $("[data-test-id='dashboard']");
    private final SelenideElement firstCard = $("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0']");
    private final SelenideElement secondCard = $("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d']");
    private final SelenideElement firstCardBalance = firstCard.$(".list__item div");
    private final SelenideElement secondCardBalance = secondCard.$(".list__item div");
    private final SelenideElement firstCardButton = firstCard.$("[data-test-id='action-deposit']");
    private final SelenideElement secondCardButton = secondCard.$("[data-test-id='action-deposit']");

    public void shouldBeVisible() {
        heading.shouldBe(visible);
    }

    public int getFirstCardBalance() {
        String text = firstCardBalance.text();
        return extractBalance(text);
    }

    public int getSecondCardBalance() {
        String text = secondCardBalance.text();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        String[] parts = text.split(":");
        String balanceString = parts[1].replaceAll("[^\\d]", "");
        return Integer.parseInt(balanceString);
    }

    public TransferPage transferToFirstCard() {
        firstCardButton.click();
        return new TransferPage();
    }

    public TransferPage transferToSecondCard() {
        secondCardButton.click();
        return new TransferPage();
    }
}