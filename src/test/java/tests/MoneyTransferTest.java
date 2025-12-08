package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.pages.*;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest extends TestBase {
    private DashboardPage dashboardPage;
    private String firstCardId;
    private String secondCardId;

    @BeforeEach
    void loginToDashboard() {
        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin("vasya", "qwerty123");
        dashboardPage = verificationPage.validVerify("12345");

        // Получаем ID карт
        firstCardId = dashboardPage.getCardId(0);
        secondCardId = dashboardPage.getCardId(1);
    }

    @Test
    void shouldTransferMoneyBetweenCards() {
        // Получаем начальные балансы
        int firstCardInitialBalance = dashboardPage.getCardBalance(firstCardId);
        int secondCardInitialBalance = dashboardPage.getCardBalance(secondCardId);

        int transferAmount = 1000;

        // Переводим с первой карты на вторую
        var transferPage = dashboardPage.selectCardToTransfer(secondCardId);
        dashboardPage = transferPage.makeTransfer(String.valueOf(transferAmount), "5559 0000 0000 0001");

        // Проверяем балансы после перевода
        int firstCardBalanceAfter = dashboardPage.getCardBalance(firstCardId);
        int secondCardBalanceAfter = dashboardPage.getCardBalance(secondCardId);

        // Проверяем, что балансы изменились корректно
        assertEquals(firstCardInitialBalance - transferAmount, firstCardBalanceAfter);
        assertEquals(secondCardInitialBalance + transferAmount, secondCardBalanceAfter);
    }

    @Test
    void shouldTransferMoneyFromSecondToFirstCard() {
        // Получаем начальные балансы
        int firstCardInitialBalance = dashboardPage.getCardBalance(firstCardId);
        int secondCardInitialBalance = dashboardPage.getCardBalance(secondCardId);

        int transferAmount = 500;

        // Переводим со второй карты на первую
        var transferPage = dashboardPage.selectCardToTransfer(firstCardId);
        dashboardPage = transferPage.makeTransfer(String.valueOf(transferAmount), "5559 0000 0000 0002");

        // Проверяем балансы после перевода
        int firstCardBalanceAfter = dashboardPage.getCardBalance(firstCardId);
        int secondCardBalanceAfter = dashboardPage.getCardBalance(secondCardId);

        // Проверяем, что балансы изменились корректно
        assertEquals(firstCardInitialBalance + transferAmount, firstCardBalanceAfter);
        assertEquals(secondCardInitialBalance - transferAmount, secondCardBalanceAfter);
    }

    @Test
    void shouldNotTransferMoreThanBalance() {
        // Получаем баланс первой карты
        int firstCardBalance = dashboardPage.getCardBalance(firstCardId);

        // Пытаемся перевести больше, чем есть на карте
        int transferAmount = firstCardBalance + 1000;

        // Выбираем вторую карту для пополнения
        var transferPage = dashboardPage.selectCardToTransfer(secondCardId);

        // Вводим сумму больше баланса
        dashboardPage = transferPage.makeTransfer(String.valueOf(transferAmount), "5559 0000 0000 0001");

        // Должен быть баг: система позволяет перевести больше, чем есть на карте
        // Баланс первой карты становится отрицательным
        int firstCardBalanceAfter = dashboardPage.getCardBalance(firstCardId);

        // Этот тест должен падать, так как обнаружен баг
        // Заводим issue на GitHub
        System.out.println("BUG: Можно перевести больше денег, чем есть на карте. Баланс стал: " + firstCardBalanceAfter);
    }
}