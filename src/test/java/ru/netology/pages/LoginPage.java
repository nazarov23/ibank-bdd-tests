package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    public VerificationPage validLogin(DataHelper.AuthInfo authInfo) {
        $("[data-test-id='login'] input").shouldBe(visible).setValue(authInfo.getLogin());
        $("[data-test-id='password'] input").setValue(authInfo.getPassword());
        $("[data-test-id='action-login']").click();
        return new VerificationPage();
    }

    public static class VerificationPage {

        public DashboardPage validVerify(DataHelper.VerificationCode verificationCode) {
            $("[data-test-id='code'] input").shouldBe(visible).setValue(verificationCode.getCode());
            $("[data-test-id='action-verify']").click();
            return new DashboardPage();
        }
    }
}