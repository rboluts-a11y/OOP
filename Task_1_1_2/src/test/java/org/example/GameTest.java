package org.example;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.Objects;

class GameTest {
    @Test
    void testConstantStrings() {
        Game game = new Game();
        assertEquals("Добро пожаловать в Блэкджек!", game.hello());
        assertEquals("Спасибо за игру! Мы будем рады видеть Вас ещё!", game.goodbye());
    }

    @Test
    void testSimulateLotScenariosNoPlayer() {
        Game game = new Game();
        for (int i = 0; i < 1000; i++) {
            String entryMessage = game.newRound();
            assertTrue(entryMessage.contains("Раунд "));
            assertTrue(entryMessage.contains("Дилер раздал карты!"));
            if (!Objects.equals(game.doIfAnyoneHasBlackjack(), "")) {
                continue;
            }
            game.doIfAnyoneHasBlackjack();
            game.processDealerTakes();
            String scoreMessage = game.getScore();
            assertTrue(scoreMessage.contains("Счет "));
        }
    }
}