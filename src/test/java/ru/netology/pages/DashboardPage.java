package ru.netology.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {

    private SelenideElement heading = $("h1");

    public DashboardPage() {
        // ÐŸÑ€Ð¾Ð²ÐµÑ€ÑÐµÐ¼, Ñ‡Ñ‚Ð¾ Ð·Ð°Ð³Ð¾Ð»Ð¾Ð²Ð¾Ðº Ð²Ð¸Ð´Ð¸Ð¼
        heading.shouldBe(visible);

        // ÐŸÑ€Ð°Ð²Ð¸Ð»ÑŒÐ½Ñ‹Ð¹ ÑÐ¸Ð½Ñ‚Ð°ÐºÑÐ¸Ñ Ð´Ð»Ñ or()
        heading.shouldHave(or("Ð—Ð°Ð³Ð¾Ð»Ð¾Ð²Ð¾Ðº Ð´Ð¾Ð»Ð¶ÐµÐ½ Ð±Ñ‹Ñ‚ÑŒ 'Ð›Ð¸Ñ‡Ð½Ñ‹Ð¹ ÐºÐ°Ð±Ð¸Ð½ÐµÑ‚' Ð¸Ð»Ð¸ 'Ð’Ð°ÑˆÐ¸ ÐºÐ°Ñ€Ñ‚Ñ‹'",
                text("Ð›Ð¸Ñ‡Ð½Ñ‹Ð¹ ÐºÐ°Ð±Ð¸Ð½ÐµÑ‚"),
                text("Ð’Ð°ÑˆÐ¸ ÐºÐ°Ñ€Ñ‚Ñ‹")
        ));
    }

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        var card = $("[data-test-id='" + cardInfo.getTestId() + "']");
        var text = card.getText();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        var start = text.indexOf("Ð±Ð°Ð»Ð°Ð½Ñ: ");
        var end = text.indexOf(" Ñ€.");
        var value = text.substring(start + 8, end).trim();
        return Integer.parseInt(value);
    }

    public TransferPage selectCardToTransfer(DataHelper.CardInfo cardInfo) {
        $("[data-test-id='" + cardInfo.getTestId() + "'] [data-test-id=action-deposit]").click();
        return new TransferPage();
    }
}
