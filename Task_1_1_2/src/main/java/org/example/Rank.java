package org.example;

/**
 * Перечисление, представляющее ранги игральных карт и их базовую стоимость.
 */
public enum Rank {
    TWO("Двойка", 2), THREE("Тройка", 3), FOUR("Четвёрка", 4), FIVE("Пятёрка", 5),
    SIX("Шестёрка", 6), SEVEN("Семёрка", 7), EIGHT("Восьмёрка", 8), NINE("Девятка", 9),
    TEN("Десятка", 10), JACK("Валет", 10), QUEEN("Дама", 10), KING("Король", 10),
    ACE("Туз", 11);

    private final String name;
    private final int value;

    Rank(String name, int value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Возвращает базовую стоимость ранга карты (для туза это 11).
     *
     * @return базовая стоимость в очках
     */
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }
}