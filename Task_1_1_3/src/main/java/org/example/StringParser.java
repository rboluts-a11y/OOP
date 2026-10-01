package org.example;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс для парсера строки с означенными переменными для построения словаря.
 */
public class StringParser {
    /**
     * Метод, который парсит строку с означенными переменными.
     *
     * @param stringVariables строковое представление строки с означенными переменными
     * @return словарь, где ключ - переменная, а значение - число.
     */
    public static Map<String, Integer> parse(String stringVariables) {
        Map<String, Integer> allVariables = new HashMap<>();
        stringVariables = stringVariables.replace(" ", "");
        String[] variables = stringVariables.split(";");
        for (String oneVariable: variables) {
            String[] variableAndValue = oneVariable.split("=");
            allVariables.put(
                    variableAndValue[0],
                    Integer.parseInt(variableAndValue[1])
            );
        }
        return allVariables;
    }
}