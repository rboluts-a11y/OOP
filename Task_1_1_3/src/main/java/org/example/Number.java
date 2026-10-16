package org.example;

import java.util.Map;

/**
 * Класс, который представляет собой выражение, являющееся числом.
 */
public class Number extends Expression {
    int numberValue;

    /**
     * Конструктор для класса Number.
     *
     * @param numberValue числовое значение
     */
    public Number(int numberValue) {
        this.numberValue = numberValue;
    }

    /**
     * Метод, который возвращает строковое представление числа.
     *
     * @return соответствующее строковое значение
     */
    @Override
    public String toString() {
        return String.valueOf(numberValue);
    }

    /**
     * Метод, который возвращает результат посимвольного дифференцирования для числа.
     * Легко заметить что производная от константы всегда равна 0.
     *
     * @param variable переменная, по которой ведётся дифференцирование
     * @return Number(0)
     */
    public Expression derivative(String variable) {
        return new Number(0);
    }

    /**
     * Метод, который вычисляет значение математического выражения при означивании переменных.
     * Легко заметить что это будет просто значение числа
     *
     * @param specifiedVariables словарь, где ключ - переменная, значение - число
     * @return значение числа
     */
    int evalByMap(Map<String, Integer> specifiedVariables) {
        return numberValue;
    }

    /**
     * Метод, который упрощает выражение, представляющее собой число
     * Для числа ничего упростить нельзя, поэтому упрощенное выражение - это сам объект.
     *
     * @return новое, упрощенное выражение
     */
    public Expression simplify() {
       return this;
    }
}