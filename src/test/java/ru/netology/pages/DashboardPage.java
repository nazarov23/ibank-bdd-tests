package ru.netology.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final ElementsCollection cards = $$(".list__item");
    private final SelenideElement dashboard = $("[data-test-id='dashboard']");

    public TransferPage selectCardToReplenish(DataHelper.CardInfo card) {
        // Используем text() вместо byText()
        cards.findBy(text("**** **** **** " + card.getLastFourDigits()))
                .$("button").click();
        return new TransferPage();
    }

    public int getCardBalance(DataHelper.CardInfo card) {
        String cardText = cards.findBy(text("**** **** **** " + card.getLastFourDigits()))
                .getText();
        return extractBalance(cardText);
    }

    private int extractBalance(String cardText) {
        // Формат: "**** **** **** 0001\nбаланс: 10000 руб."
        String balancePart = cardText.split(":")[1].trim();
        return Integer.parseInt(balancePart.replaceAll("[^0-9]", ""));
    }

    public void shouldBeVisible() {
        dashboard.shouldBe(visible);
    }
}