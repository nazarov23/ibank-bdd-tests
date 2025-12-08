package tests;

import org.junit.jupiter.api.Test;
import ru.netology.pages.*;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

public class BalanceParsingTest extends TestBase {

    @Test
    void shouldParseBalanceFromCardElement() {
        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin("vasya", "qwerty123");
        var dashboardPage = verificationPage.validVerify("12345");

        int balance = dashboardPage.getCardBalance(0);

        assertTrue(balance > 0, "Баланс должен быть положительным");
        assertEquals(10000, balance, "Начальный баланс должен быть 10000");
    }
}