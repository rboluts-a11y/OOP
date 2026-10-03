package org.example;

import java.util.Map;
import java.util.Objects;

/**
 * Класс, который представляет собой разность двух выражений.
 */
public class Sub extends Expression {
    Expression leftSide;
    Expression rightSide;

    /**
     * Конструктор для класса Sub.
     *
     * @param leftSide выражение в левой части (слева от знака минус)
     * @param rightSide выражение в правой части (справа от знака минус)
     */
    public Sub(Expression leftSide, Expression rightSide) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    /**
     * Метод, который возвращает строковое представление разности.
     *
     * @return выражение в форме (left-right)
     */
    @Override
    public String toString() {
        return "(" + leftSide + "-" + rightSide + ")";
    }

    /**
     * Метод, который возвращает результат посимвольного дифференцирования для разности.
     *
     * @param variable переменная, по которой ведётся дифференцирование
     * @return соответствующее математическое выражение
     */
    public Expression derivative(String variable) {
        return new Sub(leftSide.derivative(variable), rightSide.derivative(variable));
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
                - rightSide.evalByMap(specifiedVariables);
    }

    /**
     * Метод, который упрощает выражение, представляющее собой разность двух выражений.
     * Этот метод не изменяет исходное выражение, а только создаёт новое.
     *
     * @return новое, упрощенное выражение
     */
    public Expression simplify() {
        Expression expLeftSide = leftSide.simplify();
        Expression expRightSide = rightSide.simplify();
        if (expLeftSide instanceof Number && expRightSide instanceof Number) {
            return new Number(((Number) expLeftSide).numberValue
                    - ((Number) expRightSide).numberValue);
        } else if (Objects.equals(expLeftSide.toString(), expRightSide.toString())) {
            return new Number(0);
        } else {
            return new Sub(expLeftSide, expRightSide);
        }
    }
}