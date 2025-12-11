package ru.netology.tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BalanceParsingTest {

    @Test
    void shouldParseBalanceFromCardText() {
        String cardText = "**** **** **** 0001, баланс: 10000 р.";
        int balance = extractBalance(cardText);
        assertEquals(10000, balance);
    }

    @Test
    void shouldParseBalanceWithDifferentFormats() {
        assertEquals(10000, extractBalance("**** **** **** 0001, баланс: 10 000 р."));
        assertEquals(10000, extractBalance("**** **** **** 0001, баланс: 10,000 р."));
        assertEquals(15000, extractBalance("**** **** **** 0002, баланс: 15000 р."));
    }

    @Test
    void shouldParseBalanceWithKopecks() {
        String cardText = "**** **** **** 0001, баланс: 10000,50 р.";
        int balance = extractBalance(cardText);
        assertEquals(10000, balance); // Отбрасываем копейки
    }

    private int extractBalance(String text) {
        String balanceStart = "баланс: ";
        String balanceFinish = " р.";

        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);

        if (start == -1 || finish == -1) {
            fail("Не найден баланс в тексте: " + text);
        }

        String value = text.substring(start + balanceStart.length(), finish);
        value = value.replaceAll("[^0-9]", "");

        if (value.isEmpty()) {
            fail("Не удалось извлечь число из: " + text);
        }

        return Integer.parseInt(value);
    }
}