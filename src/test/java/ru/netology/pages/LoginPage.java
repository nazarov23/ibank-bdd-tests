package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement loginField = $("[data-test-id='login'] input");
    private final SelenideElement passwordField = $("[data-test-id='password'] input");
    private final SelenideElement loginButton = $("[data-test-id='action-login']");

    public VerificationPage validLogin(DataHelper.AuthInfo authInfo) {
        loginField.setValue(authInfo.getLogin());
        passwordField.setValue(authInfo.getPassword());
        loginButton.click();
        return new VerificationPage();
    }

    public static class VerificationPage {
        private final SelenideElement codeField = $("[data-test-id='code'] input");
        private final SelenideElement verifyButton = $("[data-test-id='action-verify']");

        public DashboardPage validVerify(DataHelper.VerificationCode verificationCode) {
            codeField.shouldBe(visible).setValue(verificationCode.getCode());
            verifyButton.click();
            return new DashboardPage();
        }
    }
}