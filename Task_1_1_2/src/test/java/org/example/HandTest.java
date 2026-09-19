package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class HandTest {
    @Test
    void testScoreAndBustedRegularCards() {
        Hand hand = new Hand();

        hand.addCard(new Card(Suit.SPADES, Rank.QUEEN));
        hand.addCard(new Card(Suit.HEARTS, Rank.SEVEN));

        assertEquals(17, hand.getScore());
        assertFalse(hand.isBusted());

        hand.addCard(new Card(Suit.CLUBS, Rank.FIVE));
        assertEquals(22, hand.getScore());
        assertTrue(hand.isBusted());
    }

    @Test
    void testScoreWithAces() {
        Hand hand = new Hand();

        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        assertEquals(11, hand.getScore());

        hand.addCard(new Card(Suit.SPADES, Rank.ACE));
        assertEquals(12, hand.getScore());

        hand.addCard(new Card(Suit.DIAMONDS, Rank.KING));
        assertEquals(12, hand.getScore());

    }

    @Test
    void testIsBlackjackScenarios() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.SPADES, Rank.TEN));
        hand.addCard(new Card(Suit.HEARTS, Rank.SEVEN));
        hand.addCard(new Card(Suit.CLUBS, Rank.FOUR));
        assertEquals(21, hand.getScore());
        assertFalse(hand.isBlackjack());

        Hand blackjackHand = new Hand();
        blackjackHand.addCard(new Card(Suit.SPADES, Rank.ACE));
        blackjackHand.addCard(new Card(Suit.CLUBS, Rank.KING));
        assertTrue(blackjackHand.isBlackjack());

        Hand handWithTwoAces = new Hand();
        handWithTwoAces.addCard(new Card(Suit.SPADES, Rank.ACE));
        handWithTwoAces.addCard(new Card(Suit.DIAMONDS, Rank.ACE));
        assertFalse(handWithTwoAces.isBlackjack());

        Hand handWithOneCard = new Hand();
        handWithOneCard.addCard(new Card(Suit.DIAMONDS, Rank.TWO));
        assertFalse(handWithOneCard.isBlackjack());
    }

    @Test
    void testAddClearAndOpenSecondCard() {
        Hand hand = new Hand();

        assertThrows(IllegalStateException.class, hand::openSecondCard);

        hand.addCard(new Card(Suit.SPADES, Rank.TWO));
        assertThrows(IllegalStateException.class, hand::openSecondCard);

        Card secondCard = new Card(Suit.HEARTS, Rank.KING);
        hand.addCard(secondCard);
        assertEquals(secondCard, hand.openSecondCard());

        hand.clear();
        assertEquals(0, hand.getScore());
        assertThrows(IllegalStateException.class, hand::openSecondCard);
    }

    @Test
    void testToStringFormattedScenarios() {
        Hand hand = new Hand();
        assertEquals("[] => 0", hand.toStringFormatted(false));

        hand.addCard(new Card(Suit.SPADES, Rank.QUEEN));
        hand.addCard(new Card(Suit.HEARTS, Rank.THREE));
        assertEquals("[Дама Пики (10), Тройка Червы (3)] => 13", hand.toStringFormatted(false));
        assertEquals("[Дама Пики (10), <закрытая карта>]", hand.toStringFormatted(true));

        Hand aceHand = new Hand();
        aceHand.addCard(new Card(Suit.SPADES, Rank.ACE));
        aceHand.addCard(new Card(Suit.CLUBS, Rank.ACE));
        String result = aceHand.toStringFormatted(false);
        String optionA = "[Туз Пики (11), Туз Трефы (1)] => 12";
        String optionB = "[Туз Пики (1), Туз Трефы (11)] => 12";

        // Проверяем, что строка соответствует хотя бы одному из логически верных вариантов
        assertTrue(result.equals(optionA) || result.equals(optionB),
                "Строка форматирования не соответствует ни одному эталону. Получено: " + result);

        Hand oneCardHand = new Hand();
        oneCardHand.addCard(new Card(Suit.DIAMONDS, Rank.JACK));
        assertEquals("[Валет Бубны (10)] => 10", oneCardHand.toStringFormatted(true));

        Hand notBustedHand = new Hand();
        notBustedHand.addCard(new Card(Suit.DIAMONDS, Rank.QUEEN));
        notBustedHand.addCard(new Card(Suit.CLUBS, Rank.TEN));
        notBustedHand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        assertEquals("[Дама Бубны (10), Десятка Трефы (10), Туз Червы (1)] => 21",
                notBustedHand.toStringFormatted(false));
    }

    @Test
    void testDefaultToString() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.DIAMONDS, Rank.SEVEN));
        assertEquals(hand.toStringFormatted(false), hand.toString());
    }
}