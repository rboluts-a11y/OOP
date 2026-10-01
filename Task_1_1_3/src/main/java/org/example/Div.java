package org.example;

import java.util.Map;

/**
 * Класс, который представляет собой частное двух выражений.
 */
public class Div extends Expression {
    Expression leftSide;
    Expression rightSide;

    /**
     * Конструктор для класса Div.
     *
     * @param leftSide выражение в левой части (слева от знака деления)
     * @param rightSide выражение в правой части (справа от знака деления)
     */
    public Div(Expression leftSide, Expression rightSide) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    /**
     * Метод, который возвращает строковое представление частного.
     *
     * @return выражение в форме (left/right)
     */
    @Override
    public String toString() {
        return "(" + leftSide + "/" + rightSide + ")";
    }

    /**
     * Метод, который возвращает результат посимвольного дифференцирования для частного.
     *
     * @param variable переменная, по которой ведётся дифференцирование
     * @return соответствующее математическое выражение
     */
    public Expression derivative(String variable) {
        Mul firstTerm = new Mul(leftSide.derivative(variable), rightSide);
        Mul secondTerm = new Mul(leftSide, rightSide.derivative(variable));
        Sub nominator = new Sub(firstTerm, secondTerm);
        Mul denominator = new Mul(rightSide, rightSide);
        return new Div(nominator, denominator);
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
                / rightSide.evalByMap(specifiedVariables);
    }
}