package org.example;

import java.util.Map;

/**
 * Класс, который представляет собой сумму двух выражений.
 */
public class Add extends Expression {
    Expression leftSide;
    Expression rightSide;

    /**
     * Конструктор для класса Add.
     *
     * @param leftSide выражение в левой части (слева от знака плюс)
     * @param rightSide выражение в правой части (справа от знака плюс)
     */
    public Add(Expression leftSide, Expression rightSide) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    /**
     * Метод, который возвращает строковое представление суммы.
     *
     * @return выражение в форме (left+right)
     */
    @Override
    public String toString() {
        return "(" + leftSide + "+" + rightSide + ")";
    }

    /**
     * Метод, который возвращает результат посимвольного дифференцирования для суммы.
     *
     * @param variable переменная, по которой ведётся дифференцирование
     * @return соответствующее математическое выражение
     */
    public Expression derivative(String variable) {
        return new Add(leftSide.derivative(variable), rightSide.derivative(variable));
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
                + rightSide.evalByMap(specifiedVariables);
    }

    /**
     * Метод, который упрощает выражение, представляющее собой сумму двух выражений.
     * Этот метод не изменяет исходное выражение, а только создаёт новое.
     *
     * @return новое, упрощенное выражение
     */
    public Expression simplify() {
        Expression expLeftSide = leftSide.simplify();
        Expression expRightSide = rightSide.simplify();
        if (expLeftSide instanceof Number && expRightSide instanceof Number) {
            return new Number(((Number) expLeftSide).numberValue
                    + ((Number) expRightSide).numberValue);
        } else {
            return new Add(expLeftSide, expRightSide);
        }
    }
}