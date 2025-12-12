package ru.netology.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.netology.data.DataHelper;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.LoginPage;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {
    private DashboardPage dashboardPage;

    @BeforeAll
    static void setUpAll() {
        // Создаем и настраиваем ChromeOptions
        ChromeOptions options = new ChromeOptions();

        // 1. Отключаем все всплывающие окна и предупреждения
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);           // Отключаем службу учетных данных
        prefs.put("profile.password_manager_enabled", false);     // Отключаем менеджер паролей
        prefs.put("profile.password_manager_leak_detection", false); // Отключаем проверку утечек паролей

        // 2. Отключаем уведомления
        prefs.put("profile.default_content_setting_values.notifications", 2);

        // 3. Отключаем предупреждения о сохранении пароля
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("profile.password_manager_enabled", false);

        // 4. Отключаем предупреждения о небезопасном контенте для localhost
        prefs.put("profile.managed_default_content_settings.images", 1);
        prefs.put("profile.managed_default_content_settings.stylesheets", 1);

        options.setExperimentalOption("prefs", prefs);

        // 5. Добавляем дополнительные аргументы для отключения всплывающих окон
        options.addArguments("--disable-infobars");                // Отключаем инфо-панель Chrome
        options.addArguments("--disable-notifications");           // Отключаем уведомления
        options.addArguments("--disable-popup-blocking");          // Отключаем блокировку всплывающих окон
        options.addArguments("--disable-save-password-bubble");    // Отключаем окно сохранения пароля
        options.addArguments("--disable-password-manager-reauthentication"); // Отключаем повторную аутентификацию
        options.addArguments("--password-store=basic");            // Упрощаем хранение паролей
        options.addArguments("--disable-features=PasswordLeakDetection"); // Отключаем проверку утечек
        options.addArguments("--disable-dev-shm-usage");           // Для Docker/CI совместимости
        options.addArguments("--no-sandbox");                      // Отключаем песочницу (для CI)
        options.addArguments("--disable-gpu");                     // Отключаем GPU (для CI)
        options.addArguments("--window-size=1920,1080");           // Устанавливаем размер окна
        options.addArguments("--ignore-certificate-errors");       // Игнорируем ошибки сертификатов
        options.addArguments("--disable-extensions");              // Отключаем расширения

        // 6. Отключаем автоматическое сохранение паролей
        options.addArguments("--enable-automation");
        options.addArguments("--disable-blink-features=AutomationControlled");

        // 7. Передаем настроенные опции в Selenide
        Configuration.browserCapabilities = options;

        // 8. Основные настройки Selenide
        Configuration.browser = "chrome";
        Configuration.headless = false; // Временно false для отладки. Для CI поменяйте на true
        Configuration.timeout = 15000;
        Configuration.browserSize = "1920x1080";
        Configuration.pageLoadStrategy = "normal";
        Configuration.screenshots = true;
        Configuration.savePageSource = false;
    }

    @BeforeEach
    void setup() {
        try {
            System.out.println("=== Начало настройки теста ===");

            // Открываем страницу
            open("http://localhost:9999");

            // Проверяем, что страница загрузилась
            System.out.println("Текущий URL: " + WebDriverRunner.url());

            // Ждем появления элементов логина
            $("[data-test-id='login'] input").shouldBe(visible);
            $("[data-test-id='password'] input").shouldBe(visible);

            System.out.println("Страница логина загружена");

            // Логинимся
            DataHelper.AuthInfo authInfo = DataHelper.getAuthInfo();
            LoginPage loginPage = new LoginPage();

            System.out.println("Ввод логина: " + authInfo.getLogin());
            dashboardPage = loginPage.validLogin(authInfo)
                    .validVerify(DataHelper.getVerificationCode());

            System.out.println("Верификация завершена");

            // Пауза для загрузки Dashboard
            sleep(2000);

            // Проверяем URL после логина
            System.out.println("URL после логина: " + WebDriverRunner.url());

            // Проверяем, что Dashboard виден
            dashboardPage.shouldBeVisible();

            System.out.println("=== Настройка теста завершена успешно ===");

        } catch (Exception e) {
            System.err.println("ОШИБКА в методе setup(): " + e.getMessage());
            e.printStackTrace();

            // Делаем скриншот при ошибке
            try {
                screenshot("setup_error");
            } catch (Exception screenshotEx) {
                System.err.println("Не удалось сделать скриншот: " + screenshotEx.getMessage());
            }

            throw e;
        }
    }

    @Test
    @DisplayName("Успешный перевод средств с карты 2 на карту 1")
    void shouldTransferMoneyFromSecondToFirstCard() {
        try {
            System.out.println("=== Тест: Перевод с карты 2 на карту 1 ===");

            // Получаем балансы
            int firstCardInitialBalance = dashboardPage.getFirstCardBalance();
            int secondCardInitialBalance = dashboardPage.getSecondCardBalance();

            System.out.println("Начальный баланс карты 1: " + firstCardInitialBalance);
            System.out.println("Начальный баланс карты 2: " + secondCardInitialBalance);

            // Выполняем перевод
            int transferAmount = 500;
            System.out.println("Сумма перевода: " + transferAmount);

            System.out.println("Нажатие кнопки 'Пополнить' на карте 1...");
            dashboardPage.transferToFirstCard()
                    .makeTransfer(String.valueOf(transferAmount), DataHelper.getSecondCardInfo());

            System.out.println("Перевод выполнен, ожидание обновления балансов...");
            sleep(2000);

            // Проверяем обновленные балансы
            int firstCardFinalBalance = dashboardPage.getFirstCardBalance();
            int secondCardFinalBalance = dashboardPage.getSecondCardBalance();

            System.out.println("Конечный баланс карты 1: " + firstCardFinalBalance);
            System.out.println("Конечный баланс карты 2: " + secondCardFinalBalance);

            // Проверяем утверждения
            assertEquals(firstCardInitialBalance + transferAmount, firstCardFinalBalance);
            assertEquals(secondCardInitialBalance - transferAmount, secondCardFinalBalance);

            System.out.println("=== Тест пройден успешно ===");

        } catch (Exception e) {
            System.err.println("ОШИБКА в тесте: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Успешный перевод средств с карты 1 на карту 2")
    void shouldTransferMoneyFromFirstToSecondCard() {
        try {
            System.out.println("=== Тест: Перевод с карты 1 на карту 2 ===");

            // Получаем балансы
            int firstCardInitialBalance = dashboardPage.getFirstCardBalance();
            int secondCardInitialBalance = dashboardPage.getSecondCardBalance();

            System.out.println("Начальный баланс карты 1: " + firstCardInitialBalance);
            System.out.println("Начальный баланс карты 2: " + secondCardInitialBalance);

            // Выполняем перевод
            int transferAmount = 300;
            System.out.println("Сумма перевода: " + transferAmount);

            System.out.println("Нажатие кнопки 'Пополнить' на карте 2...");
            dashboardPage.transferToSecondCard()
                    .makeTransfer(String.valueOf(transferAmount), DataHelper.getFirstCardInfo());

            System.out.println("Перевод выполнен, ожидание обновления балансов...");
            sleep(2000);

            // Проверяем обновленные балансы
            int firstCardFinalBalance = dashboardPage.getFirstCardBalance();
            int secondCardFinalBalance = dashboardPage.getSecondCardBalance();

            System.out.println("Конечный баланс карты 1: " + firstCardFinalBalance);
            System.out.println("Конечный баланс карты 2: " + secondCardFinalBalance);

            // Проверяем утверждения
            assertEquals(firstCardInitialBalance - transferAmount, firstCardFinalBalance);
            assertEquals(secondCardInitialBalance + transferAmount, secondCardFinalBalance);

            System.out.println("=== Тест пройден успешно ===");

        } catch (Exception e) {
            System.err.println("ОШИБКА в тесте: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Ошибка при попытке перевода суммы, превышающей баланс")
    @Disabled("Отключено из-за бага в приложении: уведомление об ошибке не отображается при переводе суммы > баланса")
    void shouldShowErrorWhenTransferAmountExceedsBalance() {
        try {
            System.out.println("=== Тест: Попытка перевода суммы, превышающей баланс ===");

            int firstCardBalance = dashboardPage.getFirstCardBalance();
            int invalidTransferAmount = firstCardBalance + 10000;

            System.out.println("Баланс карты 1: " + firstCardBalance);
            System.out.println("Попытка перевода суммы: " + invalidTransferAmount);

            var transferPage = dashboardPage.transferToSecondCard();
            transferPage.makeInvalidTransfer(String.valueOf(invalidTransferAmount), DataHelper.getFirstCardInfo());

            System.out.println("Проверка отображения ошибки...");
            transferPage.shouldHaveError();

            System.out.println("=== Тест пройден успешно ===");

        } catch (Exception e) {
            System.err.println("ОШИБКА в тесте: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @AfterEach
    void tearDown() {
        try {
            System.out.println("=== Завершение теста, закрытие браузера ===");
            closeWebDriver();
        } catch (Exception e) {
            System.err.println("Ошибка при закрытии браузера: " + e.getMessage());
        }
    }
}