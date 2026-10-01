package org.example;

import java.util.Map;
import java.util.Objects;

/**
 * Класс, который представляет собой выражение, являющееся переменной.
 */
public class Variable extends Expression {
    String variable;

    /**
     * Конструктор для класса Variable.
     *
     * @param variable строковое представление переменной
     */
    public Variable(String variable) {
        this.variable = variable;
    }

    /**
     * Метод, который возвращает строковое представление переменной.
     *
     * @return соответствующее строковое значение
     */
    @Override
    public String toString() {
        return variable;
    }

    /**
     * Метод, который возвращает результат посимвольного дифференцирования для переменной.
     * Если по ней ведется дифференцирование, то результат 1, иначе результат 0
     *
     * @param variable переменная, по которой ведётся дифференцирование
     * @return математическое выражение, представляющее собой число со значением 0 или 1
     */
    public Expression derivative(String variable) {
        if (Objects.equals(variable, this.variable)) {
            return new Number(1);
        } else {
            return new Number(0);
        }
    }

    /**
     * Метод, который вычисляет значение математического выражения при означивании переменных.
     * Легко заметить что это будет просто значение в словаре по ключу.
     *
     * @param specifiedVariables словарь, где ключ - переменная, значение - число
     * @return значение переменной
     */
    int evalByMap(Map<String, Integer> specifiedVariables) {
        return specifiedVariables.get(variable);
    }
}