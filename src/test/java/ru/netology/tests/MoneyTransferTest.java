package ru.netology.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.*;
import ru.netology.pages.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

public class MoneyTransferTest {

    @BeforeAll
    static void setupAll() {
        // НАСТРАИВАЕМ SELENIDE с нашими опциями Chrome
        Configuration.baseUrl = "http://localhost:9999";
    }

    @BeforeEach
    void setup() {
        // Очищаем куки перед каждым тестом
        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();

        // Открываем страницу
        open("/");

        // Ждём загрузки (явно)
        $("[data-test-id='login']").shouldBe(visible, java.time.Duration.ofSeconds(10));

        // Логин
        $("[data-test-id='login'] input").setValue("vasya");
        $("input[type='password']").setValue("qwerty123");
        $("button").click();

        // Верификация
        $("input[name='code']").shouldBe(visible, java.time.Duration.ofSeconds(10));
        $("input[name='code']").setValue("12345");
        $("button").click();

        // Проверка дашборда
        $("[data-test-id='dashboard']").shouldBe(visible, java.time.Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        Selenide.closeWebDriver();
    }

    @Test
    void simpleTestToCheckLogin() {
        // Просто проверяем, что логин прошёл
        assertTrue($("[data-test-id='dashboard']").exists(),
                "Дашборд должен отображаться после логина");

        // Проверяем наличие карт
        String pageText = $("body").getText();
        assertTrue(pageText.contains("0001"), "Должна быть карта 0001");
        assertTrue(pageText.contains("0002"), "Должна быть карта 0002");
    }

    @Test
    void shouldTransferMoneyFromSecondToFirstCard() {
        // Находим карту по тексту и нажимаем "Пополнить"
        $$("div").findBy(text("0001"))
                .$("button").click();

        // Ждём форму перевода
        $("[data-test-id='amount'] input").shouldBe(visible);

        // Заполняем форму
        $("[data-test-id='amount'] input").setValue("1000");
        $("[data-test-id='from'] input").setValue("5559 0000 0000 0002");
        $("[data-test-id='action-transfer']").click();

        // Проверяем успех
        $("[data-test-id='dashboard']").shouldBe(visible);
    }

    @Test
    void shouldNotTransferZeroAmount() {
        $$("div").findBy(text("0001"))
                .$("button").click();

        $("[data-test-id='amount'] input").shouldBe(visible).setValue("0");
        $("[data-test-id='from'] input").setValue("5559 0000 0000 0002");
        $("[data-test-id='action-transfer']").click();

        // Должны остаться на форме
        assertTrue($("[data-test-id='amount']").exists() ||
                        $("[data-test-id='error-notification']").exists(),
                "При нулевой сумме должна быть ошибка");
    }
}