package ru.netology.pages;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.*;

public class TestBase {

    @BeforeAll
    static void setupAll() {
        // Получаем путь к ChromeDriver из системных свойств
        String chromeDriverPath = System.getProperty("webdriver.chrome.driver");
        if (chromeDriverPath != null && !chromeDriverPath.isEmpty()) {
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);
            System.out.println("Using ChromeDriver from: " + chromeDriverPath);
        } else {
            System.out.println("WARNING: webdriver.chrome.driver system property is not set");
        }

        // Получаем путь к Chrome binary
        String chromeBinaryPath = System.getProperty("webdriver.chrome.binary");
        if (chromeBinaryPath != null && !chromeBinaryPath.isEmpty()) {
            System.setProperty("webdriver.chrome.binary", chromeBinaryPath);
        }

        // Основные настройки Selenide
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10000;
        Configuration.pageLoadStrategy = "eager";

        // Для CI устанавливаем headless = true
        String headless = System.getProperty("selenide.headless", "true");
        Configuration.headless = Boolean.parseBoolean(headless);

        // Настройки для отключения скриншотов и сохранения страниц (ускоряет тесты)
        Configuration.screenshots = false;
        Configuration.savePageSource = false;

        // Настраиваем ChromeOptions
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();

        // Ключевые настройки для отключения менеджера паролей
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);

        // Применяем настройки
        options.setExperimentalOption("prefs", prefs);

        // Добавляем аргументы из системных свойств
        String chromeArgs = System.getProperty("chromeoptions.args");
        if (chromeArgs != null && !chromeArgs.isEmpty()) {
            String[] args = chromeArgs.split(",");
            for (String arg : args) {
                options.addArguments(arg.trim());
            }
        }

        // Дополнительные аргументы для CI
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-debugging-port=9222");

        // Передаем настроенные опции в Selenide
        Configuration.browserCapabilities = options;

        System.out.println("Selenide configured with headless=" + Configuration.headless);
    }

    @BeforeEach
    void setup() {
        // Проверяем доступность приложения перед открытием
        System.out.println("Opening application at http://localhost:9999");
        try {
            open("http://localhost:9999");
            System.out.println("Successfully opened application");
        } catch (Exception e) {
            System.err.println("Failed to open application: " + e.getMessage());
            throw e;
        }
    }

    @AfterEach
    void tearDown() {
        // Закрываем браузер после каждого теста
        closeWebDriver();
    }
}