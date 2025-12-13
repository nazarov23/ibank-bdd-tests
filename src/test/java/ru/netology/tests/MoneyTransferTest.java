package ru.netology.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.selenide.AllureSelenide;
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
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUp() {
        // Убираем headless для отладки
        Configuration.headless = false;
        Configuration.browser = "chrome";
        Configuration.timeout = 10000;

        open("http://localhost:9999");

        // Логинимся
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
        // Получаем начальные балансы
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);

        // Сумма перевода
        var amount = 1000;

        // Ожидаемые балансы
        var expectedFirstCardBalance = firstCardBalance - amount;
        var expectedSecondCardBalance = secondCardBalance + amount;

        // Выполняем перевод
        var transferPage = dashboardPage.selectCardToTransfer(secondCard);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCard.getCardNumber());

        // Получаем актуальные балансы
        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCard);
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCard);

        // Проверяем
        assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        assertEquals(expectedSecondCardBalance, actualSecondCardBalance);
    }

    @Test
    @DisplayName("Перевод со второй карты на первую")
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
    @DisplayName("Отмена перевода")
    void shouldCancelTransfer() {
        var transferPage = dashboardPage.selectCardToTransfer(secondCard);
        dashboardPage = transferPage.cancelTransfer();

        // Проверяем, что вернулись на дашборд
        dashboardPage.getCardBalance(firstCard); // Если элемент найден, значит на дашборде
    }

    @AfterEach
    void tearDown() {
        com.codeborne.selenide.Selenide.closeWebDriver();
    }
}