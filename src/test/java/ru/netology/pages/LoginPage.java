package ru.netology.pages;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class LoginPage {

    public LoginPage() {
        $("[data-test-id='login']").shouldBe(visible);
        $("[data-test-id='password']").shouldBe(visible);
    }

    public VerificationPage validLogin(String login, String password) {
        $("[data-test-id='login'] input").setValue(login);
        $("[data-test-id='password'] input").setValue(password);
        $("[data-test-id='action-login']").click();

        return new VerificationPage();
    }
}