package org.example;

import java.util.Scanner;

/**
 * Главный класс Main, который нужен для взаимодействия с пользователем.
 * Показывает пользователю, что данная программа умеет делать с выражениями.
 */
public class Main {
    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(System.in, java.nio.charset.StandardCharsets.UTF_8);
        System.out.println("Введите корректное математическое выражение:");
        String expressionString = scanner.nextLine();
        Expression expression = InfixExpressionParser.parse(expressionString);
        System.out.println("Выражение со скобками вокруг каждой операции: " + expression);
        Expression simplifiedExpression = expression.simplify();
        System.out.println("Выражение после упрощений: " + simplifiedExpression);
        System.out.println("Введите имя переменной, по которой собираетесь дифференцировать: ");
        String variableToDerivative = scanner.nextLine();
        Expression expressionDerivative = simplifiedExpression.derivative(variableToDerivative);
        System.out.println("Результат посимвольного дифференцирования: " + expressionDerivative);
        Expression simplifiedExpressionDerivative = expressionDerivative.simplify();
        System.out.println("Результат дифференцирования после упрощения: "
                + simplifiedExpressionDerivative);
        System.out.println("Для вычисления значения выражения введите значения всех переменных");
        System.out.println("Разные переменные разделяйте символом ';'. ");
        String allVariablesDefines = scanner.nextLine();
        int result = expression.eval(allVariablesDefines);
        System.out.println("Значение выражения после означивания: " + result);
    }
}