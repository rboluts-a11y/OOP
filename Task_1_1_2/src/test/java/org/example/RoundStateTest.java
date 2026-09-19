package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RoundStateTest {
    @Test
    void testStringPrefixes() {
        RoundState roundState = new RoundState();
        roundState.newRound();
        assertEquals("    Ваши карты:",
                roundState.getStateInformation().substring(0, 15));
        assertTrue(roundState.getStateInformation().contains("    Карты дилера:"));
        assertEquals("Вы открыли карту ",
                roundState.playerTakeCard().substring(0, 17));
        assertEquals("Дилер открывает карту ",
                roundState.dealerTakeCard().substring(0, 22));
        assertEquals("Дилер открывает закрытую карту ",
                roundState.dealerOpenSecondCard().substring(0, 31));
    }

    @Test
    void testSimulateLotScenarios() {
        RoundState roundState = new RoundState();
        for (int numScenario = 1; numScenario <= 100; numScenario++) {
            roundState.newRound();
            if (!roundState.isPlayerWonByBlackjack()) {
                for (int i = 0; i < 2 && !roundState.isPlayerBusted(); i++) {
                    roundState.playerTakeCard();
                }
                roundState.dealerOpenSecondCard();
                while (roundState.isDealerMustToMove()) {
                    roundState.dealerTakeCard();
                    roundState.isDealerBusted();
                }
                if (!roundState.isDealerBusted()) {
                    roundState.calculateWhoWonByScore();
                }
            }
        }
    }
}