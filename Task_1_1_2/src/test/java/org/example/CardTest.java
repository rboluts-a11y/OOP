package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CardTest {
    @Test
    void testRegularCard() {
        Card sevenOfDiamonds = new Card(Card.Suit.DIAMONDS, Card.Rank.SEVEN);

        assertEquals(Card.Suit.DIAMONDS, sevenOfDiamonds.getSuit());
        assertEquals(Card.Rank.SEVEN, sevenOfDiamonds.getRank());
        assertEquals(7, sevenOfDiamonds.getPointsValue());

        assertEquals("Бубны", sevenOfDiamonds.getSuit().toString());
        assertEquals("Семёрка", sevenOfDiamonds.getRank().toString());

        assertEquals("Семёрка Бубны (7)", sevenOfDiamonds.toStringWithPoints(false));
        assertEquals("Семёрка Бубны (7)", sevenOfDiamonds.toStringWithPoints(true));
    }

    @Test
    void testAceCard() {
        Card aceOfClubs = new Card(Card.Suit.CLUBS, Card.Rank.ACE);

        assertEquals(11, aceOfClubs.getPointsValue());
        assertEquals(11, aceOfClubs.getRank().getValue());

        assertEquals("Туз Трефы (11)", aceOfClubs.toStringWithPoints(false));
        assertEquals("Туз Трефы (1)", aceOfClubs.toStringWithPoints(true));
    }
}