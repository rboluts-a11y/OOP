package org.example;

import java.util.Map;
import java.util.Objects;

/**
 * Класс, который представляет собой математическое выражение.
 */
abstract public class Expression {
    /**
     * Метод, который возвращает выражение в строковом виде, удобном для человека.
     *
     * @return выражение в строковом виде
     */
    @Override
    public abstract String toString();

    /**
     * Метод, который определяет, соответствуют ли объекты одному математическому выражению.
     *
     * @param o объект, с которым проводим сравнение
     * @return булевое значение, означающее, соответствует ли объект o этому же выражению
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Expression e = (Expression) o;
        return Objects.equals(this.toString(), e.toString());
    }

    /**
     * Метод, который печатает в стандартный поток вывода выражение, полученное через toString().
     */
    void print() {
        System.out.print(this);
    }

    /**
     * Метод, который возвращает результат посимвольного дифференцирования по заданной переменной.
     *
     * @param variable переменная, по которой ведётся дифференцирование
     * @return соответствующее математическое выражение
     */
    public abstract Expression derivative(String variable);

    /**
     * Метод, который вычисляет значение математического выражения при означивании переменных.
     * Означенные методы уже даны в словаре.
     *
     * @param specifiedVariables словарь, где ключ - переменная, значение - число
     * @return числовое значение вычисленного выражения
     */
    abstract int evalByMap(Map<String, Integer> specifiedVariables);

    /**
     * Метод, который вычисляет значение математического выражения при означивании переменных.
     * Но переменные даны в строковом формате через точку с запятой.
     *
     * @param stringVariables строка, содержащая означенные переменные
     * @return числовое значение вычисленного выражения
     */
    int eval(String stringVariables) {
        Map<String, Integer> specifiedVariables = StringParser.parse(stringVariables);
        return evalByMap(specifiedVariables);
    }

    /**
     * Метод, который упрощает исходное выражение по правилам.
     * Этот метод не изменяет исходное выражение, а только создаёт новое.
     *
     * @return новое, упрощенное выражение
     */
    public abstract Expression simplify();
}