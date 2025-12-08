package ru.netology.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$x;

public class DashboardPage {
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public int getCardBalance(String cardId) {
        var cardElement = $x("//div[@data-test-id='" + cardId + "']");
        var text = cardElement.text();
        return extractBalance(text);
    }

    public TransferPage selectCardToTransfer(String cardId) {
        $x("//div[@data-test-id='" + cardId + "']//button").click();
        return new TransferPage();
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value.trim());
    }

    public int getCardBalance(int index) {
        var text = cards.get(index).text();
        return extractBalance(text);
    }

    public String getCardId(int index) {
        var element = cards.get(index);
        // Получаем data-test-id атрибут
        return element.getAttribute("data-test-id");
    }
}