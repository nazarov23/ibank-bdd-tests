package ru.netology.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {

    private SelenideElement heading = $("h1");
    private ElementsCollection cards = $$(".list__item");

    public DashboardPage() {
        heading.shouldBe(visible).shouldHave(text("Ваши карты"));
    }

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        var card = $("[data-test-id='" + cardInfo.getTestId() + "']");
        var text = card.getText();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        var start = text.indexOf("баланс: ");
        var end = text.indexOf(" р.");
        var value = text.substring(start + 8, end).trim();
        return Integer.parseInt(value);
    }

    public TransferPage selectCardToTransfer(DataHelper.CardInfo cardInfo) {
        $("[data-test-id='" + cardInfo.getTestId() + "'] [data-test-id=action-deposit]").click();
        return new TransferPage();
    }
}