package org.example;

/**
 * Перечисление, представляющее масти игральных карт.
 */
public enum Suit {
    HEARTS("Червы"), DIAMONDS("Бубны"), CLUBS("Трефы"), SPADES("Пики");

    private final String name;

    Suit(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}