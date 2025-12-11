package ru.netology.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {
    private ElementsCollection cards = $$("[data-test-id]");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    // Метод из задания - ищет карту по последним 4 цифрам
    public int getCardBalance(String cardLastDigits) {
        SelenideElement card = findCardByLastDigits(cardLastDigits);
        String text = card.text();
        return extractBalance(text);
    }

    // Ищет карту по последним 4 цифрам (например "0001" или "0002")
    private SelenideElement findCardByLastDigits(String lastDigits) {
        for (SelenideElement card : cards) {
            if (card.getText().contains(lastDigits)) {
                return card;
            }
        }
        throw new IllegalArgumentException("Карта с последними цифрами " + lastDigits + " не найдена");
    }

    // Нажимаем "Пополнить" на карте с указанными последними цифрами
    public TransferPage selectCardToTopUp(String cardLastDigits) {
        SelenideElement card = findCardByLastDigits(cardLastDigits);
        card.$("[data-test-id='action-deposit']").click();
        return new TransferPage();
    }

    // Улучшенный парсинг баланса (разные форматы чисел)
    private int extractBalance(String text) {
        // Убираем лишние символы и получаем только часть с балансом
        String balanceStart = "баланс: ";
        String balanceFinish = " р.";

        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);

        if (start == -1 || finish == -1) {
            // Попробуем альтернативные варианты
            balanceStart = "баланс:";
            balanceFinish = "р.";
            start = text.indexOf(balanceStart);
            finish = text.indexOf(balanceFinish);
        }

        if (start == -1 || finish == -1) {
            throw new IllegalArgumentException("Не найден баланс в тексте: " + text);
        }

        String value = text.substring(start + balanceStart.length(), finish);

        // Убираем всё, кроме цифр
        value = value.replaceAll("[^0-9]", "");

        if (value.isEmpty()) {
            throw new IllegalArgumentException("Не удалось извлечь число из: " + text);
        }

        return Integer.parseInt(value);
    }

    // Проверяем, что страница загружена
    public void shouldBeLoaded() {
        $("[data-test-id='dashboard']").shouldBe(visible);
    }
}