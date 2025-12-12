package ru.netology.tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.pages.*;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {
    private DataHelper.UserInfo validUser;
    private DataHelper.CardInfo firstCard;
    private DataHelper.CardInfo secondCard;

    @BeforeAll
    static void setupAll() {
        Configuration.browser = "chrome";
        Configuration.baseUrl = "http://localhost:9999";
        Configuration.timeout = 10000;
    }

    @BeforeEach
    void setup() {
        validUser = DataHelper.getValidUser();
        firstCard = DataHelper.getFirstCard();
        secondCard = DataHelper.getSecondCard();

        open("/");

        new LoginPage()
                .validLogin(validUser)
                .validVerify(validUser)
                .shouldBeVisible();
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }

    @Test
    @DisplayName("Успешный перевод средств с карты 2 на карту 1")
    void shouldTransferMoneyBetweenCards() {
        DashboardPage dashboard = new DashboardPage();

        int initialBalanceCard1 = dashboard.getCardBalance(firstCard);
        int initialBalanceCard2 = dashboard.getCardBalance(secondCard);
        String transferAmount = "1000";

        dashboard.selectCardToReplenish(firstCard)
                .makeValidTransfer(transferAmount, secondCard)
                .shouldBeVisible();

        int finalBalanceCard1 = dashboard.getCardBalance(firstCard);
        int finalBalanceCard2 = dashboard.getCardBalance(secondCard);

        assertEquals(initialBalanceCard1 + Integer.parseInt(transferAmount), finalBalanceCard1);
        assertEquals(initialBalanceCard2 - Integer.parseInt(transferAmount), finalBalanceCard2);
    }
}