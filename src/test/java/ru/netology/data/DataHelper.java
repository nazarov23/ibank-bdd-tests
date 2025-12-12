package ru.netology.data;

public class DataHelper {

    private DataHelper() {
        // Приватный конструктор для утильного класса
    }

    // 1. Модель пользователя
    public static class UserInfo {
        private final String login;
        private final String password;
        private final String verificationCode;

        public UserInfo(String login, String password, String verificationCode) {
            this.login = login;
            this.password = password;
            this.verificationCode = verificationCode;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public String getVerificationCode() {
            return verificationCode;
        }
    }

    // 2. Модель карты
    public static class CardInfo {
        private final String cardNumber;
        private final String lastFourDigits;

        public CardInfo(String cardNumber, String lastFourDigits) {
            this.cardNumber = cardNumber;
            this.lastFourDigits = lastFourDigits;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public String getLastFourDigits() {
            return lastFourDigits;
        }
    }

    // 3. Получение тестовых данных пользователя
    public static UserInfo getValidUser() {
        return new UserInfo("vasya", "qwerty123", "12345");
    }

    public static UserInfo getInvalidUser() {
        return new UserInfo("invalid", "invalid", "00000");
    }

    // 4. Получение тестовых данных карт
    public static CardInfo getFirstCard() {
        return new CardInfo("5559 0000 0000 0001", "0001");
    }

    public static CardInfo getSecondCard() {
        return new CardInfo("5559 0000 0000 0002", "0002");
    }

    // 5. Методы для генерации сумм
    public static String getValidAmount(int baseBalance) {
        // Переводим 50% от баланса
        return String.valueOf(baseBalance / 2);
    }

    public static String getExcessiveAmount(int baseBalance) {
        // Сумма больше баланса на 1000
        return String.valueOf(baseBalance + 1000);
    }

    public static String getZeroAmount() {
        return "0";
    }

    public static String getNegativeAmount() {
        return "-100";
    }

    public static String getSmallAmount() {
        return "1"; // Минимальная сумма
    }

    // 6. Ожидаемые сообщения об ошибках
    public static String getInsufficientFundsError() {
        return "Недостаточно средств";
    }

    public static String getInvalidCardError() {
        return "Неверно указан номер карты";
    }

    public static String getSameCardError() {
        return "Нельзя перевести на ту же карту";
    }

    public static String getInvalidAmountError() {
        return "Неверно указана сумма";
    }

    public static String getEmptyFieldError() {
        return "Поле обязательно для заполнения";
    }
}