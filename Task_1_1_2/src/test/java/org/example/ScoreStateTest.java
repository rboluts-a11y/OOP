package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ScoreStateTest {
    @Test
    void testRoundResultMessages() {
        ScoreState scoreState = new ScoreState();
        assertEquals("У вас Блэкджек со старта! Вы выиграли раунд! ",
                scoreState.playerWon("У вас Блэкджек со старта!"));
        assertEquals("У вас перебор! Вы проиграли раунд. ",
                scoreState.dealerWon("У вас перебор!"));
        assertEquals("У дилера перебор! Вы выиграли раунд! ",
                scoreState.playerWon("У дилера перебор!"));
        assertEquals("Вы набрали больше очков! Вы выиграли раунд! ",
                scoreState.playerWon("Вы набрали больше очков!"));
        assertEquals("Дилер набрал больше очков. Вы проиграли раунд. ",
                scoreState.dealerWon("Дилер набрал больше очков."));
        assertEquals("Ничья в раунде (Пуш)! Очки равны. ",
                scoreState.drawPush(""));
        assertEquals("У вас и у дилера Блэкджек со старта! Ничья в раунде (Пуш)! Очки равны. ",
                scoreState.drawPush("У вас и у дилера Блэкджек со старта! "));
    }

    @Test
    void testScoreNotifications() {
        ScoreState scoreState = new ScoreState();
        assertEquals("Счет 0:0 (Ничья по раундам).",
                scoreState.getScoreNotification());
        scoreState.playerWon("");
        assertEquals("Счет 1:0 в вашу пользу.",
                scoreState.getScoreNotification());
        scoreState.drawPush("");
        assertEquals("Счет 1:0 в вашу пользу.",
                scoreState.getScoreNotification());
        scoreState.dealerWon("");
        assertEquals("Счет 1:1 (Ничья по раундам).",
                scoreState.getScoreNotification());
        scoreState.dealerWon("");
        assertEquals("Счет 1:2 в пользу дилера.",
                scoreState.getScoreNotification());
        scoreState.dealerWon("");
        assertEquals("Счет 1:3 в пользу дилера.",
                scoreState.getScoreNotification());
        scoreState.playerWon("");
        assertEquals("Счет 2:3 в пользу дилера.",
                scoreState.getScoreNotification());
    }

    @Test
    void testNumberRound() {
        ScoreState scoreState = new ScoreState();
        assertEquals("Раунд 1", scoreState.addNewGame());
        assertEquals("Раунд 2", scoreState.addNewGame());
        assertEquals("Раунд 3", scoreState.addNewGame());
    }
}