package ru.netology.data;

public class UserInfo {
    private final String login;
    private final String password;
    private final String verificationCode;

    public UserInfo(String login, String password, String verificationCode) {
        this.login = login;
        this.password = password;
        this.verificationCode = verificationCode;
    }

    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public String getVerificationCode() { return verificationCode; }
}