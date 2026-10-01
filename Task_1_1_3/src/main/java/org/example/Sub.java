package org.example;

import java.util.Map;

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
}