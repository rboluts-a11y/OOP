package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MainTest {
    @Test
    void testGetScoreNotifications() {
        String playerLeading = Main.getScoreNotification(3, 1);
        assertEquals("Счет 3:1 в вашу пользу.", playerLeading);

        String dealerLeading = Main.getScoreNotification(0, 2);
        assertEquals("Счет 0:2 в пользу дилера.", dealerLeading);

        String tieGame = Main.getScoreNotification(1, 1);
        assertEquals("Счет 1:1 (Ничья по раундам).", tieGame);
    }
}