package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private final SelenideElement codeField = $("[data-test-id='code'] input");
    private final SelenideElement verifyButton = $("[data-test-id='action-verify']");

    public DashboardPage validVerify(UserInfo user) {
        codeField.setValue(user.getVerificationCode());
        verifyButton.click();
        return new DashboardPage();
    }
}