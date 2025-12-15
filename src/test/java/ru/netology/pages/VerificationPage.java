package ru.netology.pages;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class VerificationPage {

    public VerificationPage() {
        $("[data-test-id='code']").shouldBe(visible);
    }

    public DashboardPage validVerify(String verificationCode) {
        $("[data-test-id='code'] input").setValue(verificationCode);
        $("[data-test-id='action-verify']").click();

        return new DashboardPage();
    }
}