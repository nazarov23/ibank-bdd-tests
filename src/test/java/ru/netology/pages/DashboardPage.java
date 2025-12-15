package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper.CardInfo;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {

    public DashboardPage() {
        // Verify we're on dashboard
        $("h1").shouldHave(text("Личный кабинет").or(text("Dashboard")));
        $(byText("Ваши карты")).shouldBe(visible);
    }

    public int getCardBalance(CardInfo cardInfo) {
        // Extract last 4 digits
        String lastDigits = cardInfo.getCardNumber().substring(15);

        // Find balance element
        SelenideElement cardElement = $x("//*[contains(text(),'" + lastDigits + "')]/ancestor::div[contains(@class,'card')]");
        String balanceText = cardElement.$("[class*='balance'], [class*='amount']").getText();

        return extractBalance(balanceText);
    }

    public TransferPage selectCardToTransfer(CardInfo cardInfo) {
        String lastDigits = cardInfo.getCardNumber().substring(15);

        // Find and click "Пополнить" button for specific card
        $x("//*[contains(text(),'" + lastDigits + "')]/ancestor::div[contains(@class,'card')]//button[contains(text(),'Пополнить')]")
                .shouldBe(enabled)
                .click();

        return new TransferPage();
    }

    public void verifyOnDashboard() {
        $("h1").shouldHave(text("Личный кабинет"));
    }

    private int extractBalance(String text) {
        return Integer.parseInt(text.replaceAll("[^0-9]", ""));
    }
}