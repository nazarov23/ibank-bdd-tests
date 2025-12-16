package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {

    private SelenideElement codeField = $("[data-test-id=code] input");
    private SelenideElement verifyButton = $("[data-test-id=action-verify]");

    // Конструктор с проверкой загрузки страницы
    public VerificationPage() {
        // Увеличиваем таймаут для CI (10 секунд вместо стандартных 4)
        codeField.shouldBe(visible, Duration.ofSeconds(10));
    }

    public DashboardPage validVerify(String verificationCode) {
        codeField.setValue(verificationCode);
        verifyButton.click();
        return new DashboardPage();
    }
}
