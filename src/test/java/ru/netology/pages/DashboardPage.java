package ru.netology.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {

    private SelenideElement heading = $("h1");

    public DashboardPage() {
        // Проверяем, что заголовок видим
        heading.shouldBe(visible);

        // Правильный синтаксис для or()
        heading.shouldHave(or("Заголовок должен быть 'Личный кабинет' или 'Ваши карты'",
                text("Личный кабинет"),
                text("Ваши карты")
        ));
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