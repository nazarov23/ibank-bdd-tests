package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement loginField = $("[data-test-id='login'] input");
    private SelenideElement passwordField = $("input[type='password']");
    private SelenideElement loginButton = $("button");

    public void login(String login, String password) {
        loginField.setValue(login);
        passwordField.setValue(password);
        loginButton.click();
    }

    public VerificationPage validLogin(String login, String password) {
        login(login, password);
        return new VerificationPage();
    }

    public boolean isErrorVisible() {
        return $("[data-test-id='error-notification']").isDisplayed();
    }

    public void shouldBeLoaded() {
        loginField.shouldBe(visible);
        passwordField.shouldBe(visible);
        loginButton.shouldBe(visible);
    }
}