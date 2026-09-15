package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс, представляющий игральную колоду карт.
 * Обеспечивает инициализацию стандартного набора карт, перемешивание и поштучную выдачу.
 */
public class Deck {
    private final List<Card> cards;

    /**
     * Создает пустую колоду карт на базе динамического списка ArrayList.
     */
    public Deck() {
        this.cards = new ArrayList<>();
    }

    /**
     * Сбрасывает колоду и заново наполняет её стандартным набором из 52 карт.
     */
    public void reset() {
        cards.clear();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    /**
     * Перемешивает карты, находящиеся в колоде, в случайном порядке.
     * Использует встроенный алгоритм тасования коллекций.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Выдает одну карту с вершины колоды и удаляет её из общего набора.
     *
     * @return объект извлеченной карты Card
     *
     * @throws IllegalStateException если в колоде больше нет карт для выдачи
     */
    public Card takeCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("В колоде закончились карты!");
        }
        return cards.removeLast();
    }
}