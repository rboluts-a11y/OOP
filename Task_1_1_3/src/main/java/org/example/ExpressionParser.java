package org.example;

/**
 * Класс для парсера выражения по строковому представлению для построения Expression
 */
public class ExpressionParser{
    private static boolean isSymbolBinaryOperation(char symbol) {
        return symbol == '+' || symbol == '-' || symbol == '*' || symbol == '/';
    }

    private static boolean isUnaryMinus(String str, int index) {
        if (str.charAt(index) != '-') {
            return false;
        }
        if (index == 0) {
            return true;
        }
        char previous = str.charAt(index - 1);
        return previous == '(' || previous == '+' || previous == '-' || previous == '*' || previous == '/';
    }

    /**
     * Метод, который парсит строку с математическим выражением.
     *
     * @param stringFormat строковое представление выражения
     * @return объект класса Expression, построенное по строковому представлению выражения.
     */
    public static Expression parse(String stringFormat) {
        char firstCharacter = stringFormat.charAt(0);
        if (firstCharacter == '(') {
            int stringLength = stringFormat.length();
            int currentIndex = 1;
            int depth = 1;
            while (true) {
                char currentLetter = stringFormat.charAt(currentIndex);
                if (depth == 1 && isSymbolBinaryOperation(currentLetter) && !isUnaryMinus(stringFormat, currentIndex)) {
                    break;
                }
                if (currentLetter == '(') {
                    depth++;
                }
                if (currentLetter == ')') {
                    depth--;
                }
                currentIndex++;
            }
            Expression leftSide = parse(stringFormat.substring(1, currentIndex));
            Expression rightSide = parse(stringFormat.substring(currentIndex + 1, stringLength - 1));
            char binaryOperation = stringFormat.charAt(currentIndex);
            switch (binaryOperation) {
                case '+':
                    return new Add(leftSide, rightSide);
                case '-':
                    return new Sub(leftSide, rightSide);
                case '*':
                    return new Mul(leftSide, rightSide);
                case '/':
                    return new Div(leftSide, rightSide);
                default:
                    throw new IllegalStateException("Unexpected operator: " + binaryOperation);
            }
        } else if (Character.isLetter(firstCharacter)) {
            return new Variable(stringFormat);
        } else {
            return new Number(Integer.parseInt(stringFormat));
        }
    }
}