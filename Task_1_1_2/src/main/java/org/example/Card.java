package org.example;

/**
 * Класс, представляющий неизменяемую игральную карту.
 * Хранит масть и ранг карты, а также возвращает её стоимость в очках.
 */
public class Card {


    private final Suit suit;
    private final Rank rank;

    /**
     * Конструктор, который создает новую игральную карту с заданной мастью и рангом.
     *
     * @param suit масть карты
     * @param rank ранг карты
     */
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * Возвращает масть карты.
     *
     * @return объект масти Suit
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Возвращает ранг карты.
     *
     * @return объект ранга Rank
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Возвращает базовую стоимость карты в очках на основе её ранга.
     *
     * @return количество очков за карту
     */
    public int getPointsValue() {
        return rank.getValue();
    }

    /**
     * Формирует красивое строковое представление карты с указанием её текущей ценности.
     * Позволяет динамически отображать стоимость Туза как 11 или 1 очко.
     *
     * @param isAceCheap флаг, указывающий, должен ли Туз считаться по минимальной стоимости
     * @return отформатированная строка с названием карты и её очками
     */
    public String toStringWithPoints(boolean isAceCheap) {
        int points = getPointsValue();
        if (points == 11 && isAceCheap) {
            points = 1;
        }
        return rank + " " + suit + " (" + points + ")";
    }
}