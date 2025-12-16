package ru.netology.tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    private DashboardPage dashboardPage;
    private DataHelper.CardInfo firstCard;
    private DataHelper.CardInfo secondCard;

    @BeforeEach
    void setUp() {

        open("http://localhost:9999");

        var authInfo = DataHelper.getAuthInfo();
        var verificationCode = DataHelper.getVerificationCode();

        dashboardPage = new LoginPage()
                .validLogin(authInfo.getLogin(), authInfo.getPassword())
                .validVerify(verificationCode.getCode());

        firstCard = DataHelper.getFirstCard();
        secondCard = DataHelper.getSecondCard();
    }

    @Test
    @DisplayName("ÐŸÐµÑ€ÐµÐ²Ð¾Ð´ Ñ Ð¿ÐµÑ€Ð²Ð¾Ð¹ ÐºÐ°Ñ€Ñ‚Ñ‹ Ð½Ð° Ð²Ñ‚Ð¾Ñ€ÑƒÑŽ")
    void shouldTransferFromFirstToSecond() {
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);


        var amount = 1000;

        var expectedFirstCardBalance = firstCardBalance - amount;
        var expectedSecondCardBalance = secondCardBalance + amount;


        var transferPage = dashboardPage.selectCardToTransfer(secondCard);

        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCard.getCardNumber());

        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCard);
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCard);


        assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        assertEquals(expectedSecondCardBalance, actualSecondCardBalance);
    }

    @Test
    @DisplayName("ÐŸÐµÑ€ÐµÐ²Ð¾Ð´ ÑÐ¾ Ð²Ñ‚Ð¾Ñ€Ð¾Ð¹ ÐºÐ°Ñ€Ñ‚Ñ‹ Ð½Ð° Ð¿ÐµÑ€Ð²ÑƒÑŽ")
    void shouldTransferFromSecondToFirst() {
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);


        var amount = 500;

        var expectedFirstCardBalance = firstCardBalance + amount;
        var expectedSecondCardBalance = secondCardBalance - amount;

        var transferPage = dashboardPage.selectCardToTransfer(firstCard);

        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), secondCard.getCardNumber());

        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCard);
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCard);


        assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        assertEquals(expectedSecondCardBalance, actualSecondCardBalance);
    }

    @Test
    @DisplayName("ÐžÑ‚Ð¼ÐµÐ½Ð° Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð°")
    void shouldCancelTransfer() {

        var transferPage = dashboardPage.selectCardToTransfer(secondCard);

        dashboardPage = transferPage.cancelTransfer();

        // ÐŸÑ€Ð¾Ð²ÐµÑ€ÑÐµÐ¼, Ñ‡Ñ‚Ð¾ Ð²ÐµÑ€Ð½ÑƒÐ»Ð¸ÑÑŒ Ð½Ð° Ð´Ð°ÑˆÐ±Ð¾Ñ€Ð´
        var balance = dashboardPage.getCardBalance(firstCard);
    }
}


