package ru.netology.pages;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;

public class TestBase {
    @BeforeAll
    static void setupAll() {
        // Настраиваем ChromeOptions перед всеми тестами
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();

        // Ключевые настройки для отключения менеджера паролей
        prefs.put("credentials_enable_service", false); // Отключает службу учетных данных[citation:1][citation:10]
        prefs.put("profile.password_manager_enabled", false); // Отключает менеджер паролей в профиле[citation:1][citation:6][citation:10]

        // Дополнительная настройка для подавления новых предупреждений об утечке паролей (актуально для свежих версий Chrome)
        prefs.put("profile.password_manager_leak_detection", false); // [citation:6]

        // Применяем настройки
        options.setExperimentalOption("prefs", prefs);

        // Добавляем аргументы для стабильности (опционально)
        options.addArguments("--disable-infobars"); // Устаревший, но может помочь с другими баннерами[citation:1][citation:6]
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Передаем настроенные опции в Selenide
        Configuration.browserCapabilities.setCapability(ChromeOptions.CAPABILITY, options);

        // Общие настройки Selenide
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.headless = false; // Установите true для запуска без GUI
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }
}