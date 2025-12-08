package tests;

import org.junit.jupiter.api.Test;
import ru.netology.pages.*;

import static org.junit.jupiter.api.Assertions.*;

public class NegativeAmountBugTest extends TestBase {

    @Test
    void shouldFailWhenTransferringNegativeAmount() {
        System.out.println("=== ТЕСТ: Перевод отрицательной суммы ===");

        // 1. Логинимся
        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin("vasya", "qwerty123");
        var dashboardPage = verificationPage.validVerify("12345");

        // 2. Получаем ID второй карты и начальный баланс
        String secondCardId = dashboardPage.getCardId(1);
        int initialBalance = dashboardPage.getCardBalance(secondCardId);
        System.out.println("Начальный баланс карты 2: " + initialBalance + " руб.");

        // 3. Переходим к переводу на вторую карту
        System.out.println("Открываем форму перевода для карты 2");
        var transferPage = dashboardPage.selectCardToTransfer(secondCardId);

        // 4. Вводим ОТРИЦАТЕЛЬНУЮ сумму (-100)
        System.out.println("Вводим сумму: -100");
        System.out.println("С карты: 5559 0000 0000 0001");
        dashboardPage = transferPage.makeTransfer("-100", "5559 0000 0000 0001");

        // 5. Проверяем баланс после "перевода"
        int finalBalance = dashboardPage.getCardBalance(secondCardId);
        System.out.println("Конечный баланс карты 2: " + finalBalance + " руб.");

        // 6. БАГ: Если баланс изменился - система приняла отрицательную сумму!
        if (finalBalance != initialBalance) {
            System.out.println("🚨 🚨 🚨 КРИТИЧЕСКИЙ БАГ ОБНАРУЖЕН!");
            System.out.println("Система ПРИНЯЛА отрицательную сумму (-100 руб.)!");
            System.out.println("Изменение баланса: " + (finalBalance - initialBalance) + " руб.");
            System.out.println("Это позволяет создавать деньги из воздуха!");

            // Сделайте скриншот сейчас:
            System.out.println("\n=== СДЕЛАЙТЕ СКРИНШОТЫ: ===");
            System.out.println("1. Форма перевода с введенным -100");
            System.out.println("2. Страница с измененным балансом: " + finalBalance + " руб.");
            System.out.println("3. Эта консоль с сообщением об ошибке");

            // Падение теста - доказательство бага
            fail("КРИТИЧЕСКИЙ БАГ: Перевод отрицательной суммы (-100) ПРИНЯТ!\n" +
                    "Баланс изменился: " + initialBalance + " → " + finalBalance + "\n" +
                    "Система создала " + (finalBalance - initialBalance) + " рублей из воздуха!");
        }

        System.out.println("✅ Тест прошел: отрицательная сумма не принимается");
    }
}