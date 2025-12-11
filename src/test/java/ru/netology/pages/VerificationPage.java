package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private SelenideElement codeField = $("input[name='code']");
    private SelenideElement verifyButton = $("button");

    public void verify(String code) {
        codeField.setValue(code);
        verifyButton.click();
    }

    public DashboardPage validVerify(String code) {
        verify(code);
        return new DashboardPage();
    }

    public boolean isErrorVisible() {
        return $("[data-test-id='error-notification']").isDisplayed();
    }

    public void shouldBeLoaded() {
        codeField.shouldBe(visible);
        verifyButton.shouldBe(visible);
    }
}