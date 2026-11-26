package org.example;

/**
 * Возможные результаты аттестации по учебной дисциплине.
 */
public enum Grade {
    EXCELLENT(5),
    GOOD(4),
    SATISFACTORY(3),
    FAIL(2),
    PASS(-1);

    private final int value;

    /**
     * Создаёт новую оценку.
     *
     * @param value числовое значение оценки
     */
    Grade(int value) {
        this.value = value;
    }

    /**
     * Возвращает числовое представление результата.
     *
     * @return числовое значение оценки
     */
    public int getValue() {
        return value;
    }
}