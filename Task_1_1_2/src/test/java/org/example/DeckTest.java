package org.example;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DeckTest {

    @Test
    void testShuffleChangesOrder() {
        Deck deck1 = new Deck();
        Deck deck2 = new Deck();

        deck1.reset();
        deck2.reset();

        deck1.shuffle();

        boolean elementsAreDifferent = false;

        for (int i = 0; i < 52; i++) {
            Card card1 = deck1.takeCard();
            Card card2 = deck2.takeCard();

            if (card1.getSuit() != card2.getSuit() || card1.getRank() != card2.getRank()) {
                elementsAreDifferent = true;
                break;
            }
        }

        assertTrue(elementsAreDifferent);
    }

    @Test
    void testDeckDepletionAndResetWorkflow() {
        Deck deck = new Deck();
        deck.reset();
        for (int i = 0; i < 52; i++) {
            Card card = deck.takeCard();
            assertNotNull(card);
        }
        Card cardAfterReset = deck.takeCard();
        assertNotNull(cardAfterReset);
    }
}
