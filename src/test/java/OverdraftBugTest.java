package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.pages.*;

import static org.junit.jupiter.api.Assertions.*;

public class OverdraftBugTest extends TestBase {
    private DashboardPage dashboardPage;
    private String firstCardId;
    private String secondCardId;

    @BeforeEach
    void loginToDashboard() {
        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin("vasya", "qwerty123");
        dashboardPage = verificationPage.validVerify("12345");

        // Получаем ID карт
        firstCardId = dashboardPage.getCardId(0);  // Карта 1: 5559 0000 0000 0001
        secondCardId = dashboardPage.getCardId(1); // Карта 2: 5559 0000 0000 0002
    }

    @Test
    void shouldNotTransferMoreThanBalance() {
        // 1. Получаем начальные балансы
        int firstCardInitial = dashboardPage.getCardBalance(firstCardId);
        int secondCardInitial = dashboardPage.getCardBalance(secondCardId);

        System.out.println("=== НАЧАЛЬНЫЕ БАЛАНСЫ ===");
        System.out.println("Карта 1 (5559 0000 0000 0001): " + firstCardInitial + " руб.");
        System.out.println("Карта 2 (5559 0000 0000 0002): " + secondCardInitial + " руб.");

        // 2. Пытаемся перевести 11000 (больше баланса карты 2!)
        System.out.println("\n=== ВЫПОЛНЯЕМ ПЕРЕВОД ===");
        System.out.println("Пополняем Карту 1 на 11000 руб. с Карты 2");

        var transferPage = dashboardPage.selectCardToTransfer(firstCardId);
        dashboardPage = transferPage.makeTransfer("11000", "5559 0000 0000 0002");

        // 3. Получаем конечные балансы
        int firstCardFinal = dashboardPage.getCardBalance(firstCardId);
        int secondCardFinal = dashboardPage.getCardBalance(secondCardId);

        System.out.println("\n=== КОНЕЧНЫЕ БАЛАНСЫ ===");
        System.out.println("Карта 1: " + firstCardFinal + " руб.");
        System.out.println("Карта 2: " + secondCardFinal + " руб.");

        // 4. Проверяем результаты
        System.out.println("\n=== РЕЗУЛЬТАТЫ ===");
        System.out.println("Изменение Карты 1: " + (firstCardFinal - firstCardInitial) + " руб.");
        System.out.println("Изменение Карты 2: " + (secondCardFinal - secondCardInitial) + " руб.");

        // 5. Проверяем наличие бага
        if (secondCardFinal < 0) {
            System.out.println("\n🚨🚨🚨 КРИТИЧЕСКИЙ БАГ ОБНАРУЖЕН! 🚨🚨🚨");
            System.out.println("Карта 2 имеет ОТРИЦАТЕЛЬНЫЙ баланс: " + secondCardFinal + " руб.");
            System.out.println("Система позволила перевести больше денег, чем есть на карте!");

            // Текст для баг-репорта
            System.out.println("\n=== ДАННЫЕ ДЛЯ БАГ-РЕПОРТА ===");
            System.out.println("Начальные балансы:");
            System.out.println("- Карта 1: " + firstCardInitial + " руб.");
            System.out.println("- Карта 2: " + secondCardInitial + " руб.");
            System.out.println("\nПеревод: 11000 руб. с Карты 2 на Карту 1");
            System.out.println("\nКонечные балансы:");
            System.out.println("- Карта 1: " + firstCardFinal + " руб. (увеличение на " +
                    (firstCardFinal - firstCardInitial) + " руб.)");
            System.out.println("- Карта 2: " + secondCardFinal + " руб. (уменьшение на " +
                    (secondCardInitial - secondCardFinal) + " руб.)");
            System.out.println("\nОЖИДАЕМО: Ошибка 'Недостаточно средств'");
            System.out.println("ФАКТИЧЕСКИ: Перевод выполнен, баланс Карты 2 отрицательный");
        }

        // 6. Assert для доказательства бага (тест должен упасть здесь)
        assertEquals(secondCardInitial, secondCardFinal,
                "КРИТИЧЕСКИЙ БАГ: Баланс Карты 2 изменился с " + secondCardInitial +
                        " на " + secondCardFinal + " руб. после перевода 11000 руб. " +
                        "(больше начального баланса!)");
    }

    // Дополнительный тест для проверки после фикса
    @Test
    void shouldTransferValidAmount() {
        // Этот тест проверяет нормальный перевод (должен проходить)
        int firstCardInitial = dashboardPage.getCardBalance(firstCardId);
        int secondCardInitial = dashboardPage.getCardBalance(secondCardId);

        // Переводим валидную сумму (меньше баланса)
        var transferPage = dashboardPage.selectCardToTransfer(firstCardId);
        dashboardPage = transferPage.makeTransfer("5000", "5559 0000 0000 0002");

        int firstCardFinal = dashboardPage.getCardBalance(firstCardId);
        int secondCardFinal = dashboardPage.getCardBalance(secondCardId);

        // Проверяем корректность перевода
        assertEquals(firstCardInitial + 5000, firstCardFinal,
                "Баланс Карты 1 должен увеличиться на 5000");
        assertEquals(secondCardInitial - 5000, secondCardFinal,
                "Баланс Карты 2 должен уменьшиться на 5000");
    }
}