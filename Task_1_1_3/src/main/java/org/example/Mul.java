package org.example;

import java.util.Map;

/**
 * Класс, который представляет собой произведение двух выражений.
 */
public class Mul extends Expression {
    Expression leftSide;
    Expression rightSide;

    /**
     * Конструктор для класса Mul.
     *
     * @param leftSide выражение в левой части (слева от знака умножения)
     * @param rightSide выражение в правой части (справа от знака умножения)
     */
    public Mul(Expression leftSide, Expression rightSide) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    /**
     * Метод, который возвращает строковое представление произведения.
     *
     * @return выражение в форме (left*right)
     */
    @Override
    public String toString() {
        return "(" + leftSide + "*" + rightSide + ")";
    }

    /**
     * Метод, который возвращает результат посимвольного дифференцирования для произведения.
     *
     * @param variable переменная, по которой ведётся дифференцирование
     * @return соответствующее математическое выражение
     */
    public Expression derivative(String variable) {
        Mul firstTerm = new Mul(leftSide.derivative(variable), rightSide);
        Mul secondTerm = new Mul(leftSide, rightSide.derivative(variable));
        return new Add(firstTerm, secondTerm);
    }

    /**
     * Метод, который вычисляет значение математического выражения при означивании переменных.
     * Означенные методы уже даны в словаре.
     *
     * @param specifiedVariables словарь, где ключ - переменная, значение - число
     * @return числовое значение вычисленного выражения
     */
    int evalByMap(Map<String, Integer> specifiedVariables) {
        return leftSide.evalByMap(specifiedVariables)
                * rightSide.evalByMap(specifiedVariables);
    }

    /**
     * Метод, который упрощает выражение, представляющее собой произведение двух выражений.
     * Этот метод не изменяет исходное выражение, а только создаёт новое.
     *
     * @return новое, упрощенное выражение
     */
    public Expression simplify() {
        Expression expLeftSide = leftSide.simplify();
        Expression expRightSide = rightSide.simplify();
        if (expLeftSide instanceof Number && expRightSide instanceof Number) {
            return new Number(((Number) expLeftSide).numberValue
                    * ((Number) expRightSide).numberValue);
        } else if (expLeftSide instanceof Number && ((Number) expLeftSide).numberValue == 0) {
            return new Number(0);
        } else if (expRightSide instanceof Number && ((Number) expRightSide).numberValue == 0) {
            return new Number(0);
        } else if (expLeftSide instanceof Number && ((Number) expLeftSide).numberValue == 1) {
            return expRightSide;
        } else if (expRightSide instanceof Number && ((Number) expRightSide).numberValue == 1) {
            return expLeftSide;
        } else {
            return new Mul(expLeftSide, expRightSide);
        }
    }
}