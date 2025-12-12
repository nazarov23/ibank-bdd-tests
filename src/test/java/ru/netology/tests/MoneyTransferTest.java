package ru.netology.tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.LoginPage;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {
    private DashboardPage dashboardPage;

    @BeforeAll
    static void setUpAll() {
        Configuration.browser = "chrome";
        Configuration.headless = true;
        Configuration.timeout = 15000;
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");

        DataHelper.AuthInfo authInfo = DataHelper.getAuthInfo();
        LoginPage loginPage = new LoginPage();
        dashboardPage = loginPage.validLogin(authInfo)
                .validVerify(DataHelper.getVerificationCode());

        dashboardPage.shouldBeVisible();
    }

    @Test
    @DisplayName("Успешный перевод средств с карты 2 на карту 1")
    void shouldTransferMoneyFromSecondToFirstCard() {
        int firstCardInitialBalance = dashboardPage.getFirstCardBalance();
        int secondCardInitialBalance = dashboardPage.getSecondCardBalance();

        int transferAmount = 500;
        dashboardPage.transferToFirstCard()
                .makeTransfer(String.valueOf(transferAmount), DataHelper.getSecondCardInfo());

        int firstCardFinalBalance = dashboardPage.getFirstCardBalance();
        int secondCardFinalBalance = dashboardPage.getSecondCardBalance();

        assertEquals(firstCardInitialBalance + transferAmount, firstCardFinalBalance);
        assertEquals(secondCardInitialBalance - transferAmount, secondCardFinalBalance);
    }

    @Test
    @DisplayName("Успешный перевод средств с карты 1 на карту 2")
    void shouldTransferMoneyFromFirstToSecondCard() {
        int firstCardInitialBalance = dashboardPage.getFirstCardBalance();
        int secondCardInitialBalance = dashboardPage.getSecondCardBalance();

        int transferAmount = 300;
        dashboardPage.transferToSecondCard()
                .makeTransfer(String.valueOf(transferAmount), DataHelper.getFirstCardInfo());

        int firstCardFinalBalance = dashboardPage.getFirstCardBalance();
        int secondCardFinalBalance = dashboardPage.getSecondCardBalance();

        assertEquals(firstCardInitialBalance - transferAmount, firstCardFinalBalance);
        assertEquals(secondCardInitialBalance + transferAmount, secondCardFinalBalance);
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }
}