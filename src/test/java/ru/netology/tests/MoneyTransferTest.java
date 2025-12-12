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
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void setup() {
        // Очистить cookies и localStorage
        clearBrowserCookies();
        clearBrowserLocalStorage();

        // Открыть страницу логина
        open("http://localhost:9999");

        // Выполнить логин и верификацию
        DataHelper.AuthInfo authInfo = DataHelper.getAuthInfo();
        LoginPage loginPage = new LoginPage();
        dashboardPage = loginPage.validLogin(authInfo)
                .validVerify(DataHelper.getVerificationCode());

        // Проверить, что Dashboard виден
        dashboardPage.shouldBeVisible();
    }

    @Test
    @DisplayName("Успешный перевод средств с карты 2 на карту 1")
    void shouldTransferMoneyFromSecondToFirstCard() {
        // Получить начальные балансы
        int firstCardInitialBalance = dashboardPage.getFirstCardBalance();
        int secondCardInitialBalance = dashboardPage.getSecondCardBalance();

        // Выполнить перевод
        int transferAmount = 500;
        dashboardPage.transferToFirstCard()
                .makeTransfer(String.valueOf(transferAmount), DataHelper.getSecondCardInfo());

        // Проверить обновленные балансы
        int firstCardFinalBalance = dashboardPage.getFirstCardBalance();
        int secondCardFinalBalance = dashboardPage.getSecondCardBalance();

        assertEquals(firstCardInitialBalance + transferAmount, firstCardFinalBalance);
        assertEquals(secondCardInitialBalance - transferAmount, secondCardFinalBalance);
    }

    @Test
    @DisplayName("Успешный перевод средств с карты 1 на карту 2")
    void shouldTransferMoneyFromFirstToSecondCard() {
        // Получить начальные балансы
        int firstCardInitialBalance = dashboardPage.getFirstCardBalance();
        int secondCardInitialBalance = dashboardPage.getSecondCardBalance();

        // Выполнить перевод
        int transferAmount = 300;
        dashboardPage.transferToSecondCard()
                .makeTransfer(String.valueOf(transferAmount), DataHelper.getFirstCardInfo());

        // Проверить обновленные балансы
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