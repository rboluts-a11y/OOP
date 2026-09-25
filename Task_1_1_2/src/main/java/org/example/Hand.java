package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс руки игрока или дилера. Отвечает за хранение карт,
 * адаптивный подсчет очков и красивое форматирование вывода.
 */
public class Hand {
    private final List<Card> cards;

    /**
     * Конструктор, который создаёт пустую руку в начале раунда.
     */
    public Hand() {
        this.cards = new ArrayList<>();
    }

    /**
     * Добавляет карту в руку.
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Очищает руку между раундами.
     */
    public void clear() {
        cards.clear();
    }

    /**
     * Возвращает вторую карту в руке для её открытия.
     * Используется дилером для раскрытия своей скрытой карты в начале хода.
     *
     * @return объект второй игральной карты Card
     *
     * @throws IllegalStateException если в руке меньше двух карт и открыть вторую невозможно
     */
    public Card openSecondCard() {
        if (cards.size() < 2) {
            throw new IllegalStateException("Невозможно открыть закрытую карту!");
        }
        return cards.get(1);
    }

    /**
     * Вычисляет и возвращает точную сумму очков всех карт в руке, адаптируя по ходу ценность тузов.
     *
     * @return итоговая сумма очков в текущей руке
     */
    public int getScore() {
        int totalScore = 0;
        int aceCount = 0;

        for (Card card : cards) {
            totalScore += card.getPointsValue();
            if (card.getRank() == Rank.ACE) {
                aceCount++;
            }
        }

        while (totalScore > 21 && aceCount > 0) {
            totalScore -= 10;
            aceCount--;
        }

        return totalScore;
    }

    /**
     * Проверяет, набрала ли текущая рука перебор по очкам.
     *
     * @return true, если суммарное количество очков в руке строго больше 21
     */
    public boolean isBusted() {
        return getScore() > 21;
    }

    /**
     * Проверяет, собрана ли в руке комбинация "Блэкджек".
     *
     * @return true, если у обладателя руки Блэкджек со старта раунда
     */
    public boolean isBlackjack() {
        return cards.size() == 2 && getScore() == 21;
    }

    /**
     * Метод строит красивое текстовое представление руки строго по ТЗ.
     * Умеет автоматически пересчитывать, как пишется Туз: (11) или (1).
     *
     * @param hideSecondCard если true, скрывает ВТОРУЮ карту (подставляя заглушку)
     * @return форматированная строка, например: "[Пиковая дама (10), Тройка Червы (3)] => 13"
     */
    public String toStringFormatted(boolean hideSecondCard) {
        if (cards.isEmpty()) {
            return "[] => 0";
        }

        if (hideSecondCard && cards.size() >= 2) {
            Card firstCard = cards.get(0);
            int firstCardPoints = firstCard.getPointsValue();
            return "[" + firstCard.getRank() + " " + firstCard.getSuit()
                    + " (" + firstCardPoints + "), <закрытая карта>]";
        }

        int totalScore = 0;
        int[] cardPoints = new int[cards.size()];

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            cardPoints[i] = card.getPointsValue();
            totalScore += cardPoints[i];
        }

        for (int i = cards.size() - 1; i >= 0; i--) {
            if (totalScore <= 21) {
                break;
            }
            if (cards.get(i).getRank() == Rank.ACE) {
                cardPoints[i] = 1;
                totalScore -= 10;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            sb.append(card.getRank()).append(" ").append(card.getSuit())
                    .append(" (").append(cardPoints[i]).append(")");

            if (i < cards.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("] => ").append(totalScore);

        return sb.toString();
    }

    /**
     * Определяет, является ли последняя карта в колоде Тузом с одним очком
     * Нужно, чтобы правильно выводить карту, которую только что раздали
     *
     * @return соответствующее булевое значение
     */
    public boolean isLastAceAndCheap() {
        int totalScore = 0;
        int[] cardPoints = new int[cards.size()];

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            cardPoints[i] = card.getPointsValue();
            totalScore += cardPoints[i];
        }

        for (int i = cards.size() - 1; i >= 0; i--) {
            if (totalScore <= 21) {
                break;
            }
            if (cards.get(i).getRank() == Rank.ACE) {
                cardPoints[i] = 1;
                totalScore -= 10;
            }
        }

        return cardPoints[cards.size() - 1] == 1;
    }

    @Override
    public String toString() {
        return toStringFormatted(false);
    }
}
