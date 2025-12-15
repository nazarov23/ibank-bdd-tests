package ru.netology.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.LoginPage;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    private DashboardPage dashboardPage;
    private DataHelper.CardInfo firstCard;
    private DataHelper.CardInfo secondCard;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUp() {
        // Configuration for CI
        Configuration.headless = true;
        Configuration.browser = "chrome";
        Configuration.timeout = 10000;
        Configuration.pageLoadTimeout = 30000;

        // Open application
        open("http://localhost:9999");

        // Login
        var authInfo = DataHelper.getAuthInfo();
        var verificationCode = DataHelper.getVerificationCode();

        dashboardPage = new LoginPage()
                .validLogin(authInfo.getLogin(), authInfo.getPassword())
                .validVerify(verificationCode.getCode());

        firstCard = DataHelper.getFirstCard();
        secondCard = DataHelper.getSecondCard();

        // Wait for dashboard to load
        Selenide.sleep(2000);
    }

    @Test
    @DisplayName("Перевод с первой карты на вторую")
    void shouldTransferFromFirstToSecond() {
        // Get initial balances
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);

        // Transfer amount
        var amount = 1000;

        // Expected balances
        var expectedFirstCardBalance = firstCardBalance - amount;
        var expectedSecondCardBalance = secondCardBalance + amount;

        // Execute transfer
        System.out.println("Starting transfer from " + firstCard.getCardNumber() + " to " + secondCard.getCardNumber());

        var transferPage = dashboardPage.selectCardToTransfer(secondCard);

        // Wait for transfer page
        Selenide.sleep(2000);

        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCard.getCardNumber());

        // Get actual balances
        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCard);
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCard);

        // Verify
        assertEquals(expectedFirstCardBalance, actualFirstCardBalance,
                "Баланс первой карты не совпадает");
        assertEquals(expectedSecondCardBalance, actualSecondCardBalance,
                "Баланс второй карты не совпадает");

        System.out.println("Transfer completed successfully");
    }

    @Test
    @DisplayName("Перевод со второй карты на первую")
    void shouldTransferFromSecondToFirst() {
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);

        var amount = 500;

        var expectedFirstCardBalance = firstCardBalance + amount;
        var expectedSecondCardBalance = secondCardBalance - amount;

        System.out.println("Starting transfer from " + secondCard.getCardNumber() + " to " + firstCard.getCardNumber());

        var transferPage = dashboardPage.selectCardToTransfer(firstCard);
        Selenide.sleep(2000);

        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), secondCard.getCardNumber());

        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCard);
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCard);

        assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        assertEquals(expectedSecondCardBalance, actualSecondCardBalance);
    }

    @Test
    @DisplayName("Отмена перевода")
    void shouldCancelTransfer() {
        System.out.println("Testing transfer cancellation...");

        var transferPage = dashboardPage.selectCardToTransfer(secondCard);
        Selenide.sleep(2000);

        dashboardPage = transferPage.cancelTransfer();

        // Verify we're back on dashboard
        dashboardPage.verifyOnDashboard();

        System.out.println("Transfer cancelled successfully");
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }
}