package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement loginField = $("[data-test-id='login'] input");
    private final SelenideElement passwordField = $("[data-test-id='password'] input");
    private final SelenideElement loginButton = $("[data-test-id='action-login']");

    public VerificationPage validLogin(UserInfo user) {
        loginField.setValue(user.getLogin());
        passwordField.setValue(user.getPassword());
        loginButton.click();
        return new VerificationPage();
    }
}