package ru.netology.data;

import lombok.Value;

public class DataHelper {

    private DataHelper() {}

    // 1. Данные пользователя
    public static UserInfo getValidUser() {
        return new UserInfo("vasya", "qwerty123", "12345");
    }

    public static UserInfo getInvalidUser() {
        return new UserInfo("invalid", "invalid", "00000");
    }

    // 2. Данные карт
    @Value
    public static class CardInfo {
        String cardNumber;
        String lastFourDigits;
    }

    public static CardInfo getFirstCard() {
        return new CardInfo("5559 0000 0000 0001", "0001");
    }

    public static CardInfo getSecondCard() {
        return new CardInfo("5559 0000 0000 0002", "0002");
    }

    // 3. Суммы для переводов
    public static String getValidAmount(int baseBalance) {
        return String.valueOf(baseBalance / 2); // 50% от баланса
    }

    public static String getExcessiveAmount(int baseBalance) {
        return String.valueOf(baseBalance + 1000); // На 1000 больше баланса
    }

    public static String getZeroAmount() {
        return "0";
    }

    public static String getNegativeAmount() {
        return "-100";
    }

    // 4. Ошибки (ожидаемые сообщения)
    public static String getInsufficientFundsError() {
        return "Недостаточно средств";
    }

    public static String getInvalidCardError() {
        return "Неверно указан номер карты";
    }

    public static String getSameCardError() {
        return "Нельзя перевести на ту же карту";
    }
}