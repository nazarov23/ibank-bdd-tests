package ru.netology.tests;

import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.WebDriverManager;
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

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        Configuration.headless = false;
        Configuration.browser = "chrome";
        Configuration.timeout = 10000;

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
    @DisplayName("Перевод с первой карты на вторую")
    void shouldTransferFromFirstToSecond() {
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);

        System.out.println("=== Начало теста: Перевод с первой карты на вторую ===");
        System.out.println("Начальный баланс первой карты: " + firstCardBalance);
        System.out.println("Начальный баланс второй карты: " + secondCardBalance);

        var amount = 1000;
        System.out.println("Сумма перевода: " + amount);

        var expectedFirstCardBalance = firstCardBalance - amount;
        var expectedSecondCardBalance = secondCardBalance + amount;

        System.out.println("Ожидаемый баланс первой карты после перевода: " + expectedFirstCardBalance);
        System.out.println("Ожидаемый баланс второй карты после перевода: " + expectedSecondCardBalance);

        var transferPage = dashboardPage.selectCardToTransfer(secondCard);
        System.out.println("Открыта страница перевода");

        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCard.getCardNumber());
        System.out.println("Перевод выполнен");

        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCard);
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCard);

        System.out.println("Фактический баланс первой карты: " + actualFirstCardBalance);
        System.out.println("Фактический баланс второй карты: " + actualSecondCardBalance);
        System.out.println("=== Конец теста ===");

        assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        assertEquals(expectedSecondCardBalance, actualSecondCardBalance);
    }

    @Test
    @DisplayName("Перевод со второй карты на первую")
    void shouldTransferFromSecondToFirst() {
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);

        System.out.println("=== Начало теста: Перевод со второй карты на первую ===");
        System.out.println("Начальный баланс первой карты: " + firstCardBalance);
        System.out.println("Начальный баланс второй карты: " + secondCardBalance);

        var amount = 500;
        System.out.println("Сумма перевода: " + amount);

        var expectedFirstCardBalance = firstCardBalance + amount;
        var expectedSecondCardBalance = secondCardBalance - amount;

        var transferPage = dashboardPage.selectCardToTransfer(firstCard);
        System.out.println("Открыта страница перевода");

        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), secondCard.getCardNumber());
        System.out.println("Перевод выполнен");

        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCard);
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCard);

        System.out.println("Фактический баланс первой карты: " + actualFirstCardBalance);
        System.out.println("Фактический баланс второй карты: " + actualSecondCardBalance);
        System.out.println("=== Конец теста ===");

        assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        assertEquals(expectedSecondCardBalance, actualSecondCardBalance);
    }

    @Test
    @DisplayName("Отмена перевода")
    void shouldCancelTransfer() {
        System.out.println("=== Начало теста: Отмена перевода ===");

        var transferPage = dashboardPage.selectCardToTransfer(secondCard);
        System.out.println("Открыта страница перевода");

        dashboardPage = transferPage.cancelTransfer();
        System.out.println("Перевод отменен, вернулись на дашборд");

        // Проверяем, что вернулись на дашборд
        var balance = dashboardPage.getCardBalance(firstCard);
        System.out.println("Баланс первой карты после отмены: " + balance);
        System.out.println("=== Конец теста ===");
    }

    @AfterEach
    void tearDown() {
        com.codeborne.selenide.Selenide.closeWebDriver();
    }
}