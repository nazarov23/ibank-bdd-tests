package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper.CardInfo;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
    private final SelenideElement pageTitle = $("h2[data-test-id='dashboard']");
    private final SelenideElement cardsTitle = $("h1");

    public DashboardPage() {
        verifyPageIsLoaded();
    }

    private void verifyPageIsLoaded() {
        pageTitle.shouldHave(text("Личный кабинет"));
        cardsTitle.shouldHave(exactText("Ваши карты"));
    }

    // Метод для выбора карты по CardInfo
    public TransferPage selectCardToTransfer(CardInfo card) {
        // Кликаем по кнопке "Пополнить" для выбранной карты
        $(String.format("[data-test-id='%s'] [data-test-id='action-deposit']", card.getTestId()))
                .click();
        return new TransferPage();
    }

    // Метод для получения баланса по CardInfo
    public int getCardBalance(CardInfo card) {
        String cardText = $(String.format("[data-test-id='%s']", card.getTestId())).getText();
        return extractBalance(cardText);
    }

    private int extractBalance(String text) {
        try {
            String balancePart = text.split("баланс:")[1].trim();
            String numberStr = balancePart.replaceAll("[^0-9]", "").trim();
            return Integer.parseInt(numberStr);
        } catch (Exception e) {
            return 0;
        }
    }
}