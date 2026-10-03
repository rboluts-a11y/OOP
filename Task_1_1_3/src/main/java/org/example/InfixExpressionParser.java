package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Класс для парсера выражения по строковому представлению для построения Expression.
 * Отличие от ExpressionParser в том, что он не требует скобки вокруг каждого выражения.
 */
public class InfixExpressionParser {
    private static boolean isUnaryMinus(List<Token> tokens) {
        if (tokens.isEmpty()) {
            return true;
        }

        TokenType previousType = tokens.get(tokens.size() - 1).getTokenType();

        return previousType == TokenType.UNARY_MINUS || previousType == TokenType.LEFT_PAREN;
    }

    private static List<Token> tokenize(String expression) {
        List<Token> tokens = new ArrayList<>();
        int index = 0;
        while (index < expression.length()) {
            char current = expression.charAt(index);
            if (Character.isWhitespace(current)) {
                index++;
                continue;
            }
            if (Character.isDigit(current)) {
                int start = index;
                while (index < expression.length()
                        && Character.isDigit(expression.charAt(index))) {
                    index++;
                }
                String value = expression.substring(start, index);
                tokens.add(new Token(TokenType.NUMBER, value));
                continue;
            }
            if (Character.isLetter(current)) {
                int start = index;
                while (index < expression.length()
                        && Character.isLetter(expression.charAt(index))) {
                    index++;
                }
                String value = expression.substring(start, index);
                tokens.add(new Token(TokenType.VARIABLE, value));
                continue;
            }
            switch (current) {
                case '+':
                    tokens.add(new Token(TokenType.PLUS, null));
                    break;
                case '-':
                    if (isUnaryMinus(tokens)) {
                        tokens.add(new Token(TokenType.UNARY_MINUS, null));
                    } else {
                        tokens.add(new Token(TokenType.MINUS, null));
                    }
                    break;
                case '*':
                    tokens.add(new Token(TokenType.MULTIPLY, null));
                    break;
                case '/':
                    tokens.add(new Token(TokenType.DIVIDE, null));
                    break;
                case '(':
                    tokens.add(new Token(TokenType.LEFT_PAREN, null));
                    break;
                case ')':
                    tokens.add(new Token(TokenType.RIGHT_PAREN, null));
                    break;
            }
            index++;
        }
        return tokens;
    }

    private static int precedence(TokenType type) {
        switch (type) {
            case PLUS:
            case MINUS:
                return 1;
            case MULTIPLY:
            case DIVIDE:
                return 2;
            case UNARY_MINUS:
                return 3;
            default:
                return -1;
        }
    }

    private static List<Token> toPostfix(List<Token> tokens) {
        List<Token> postfix = new ArrayList<>();
        Stack<Token> operators = new Stack<>();
        for (Token token : tokens) {
            TokenType type = token.getTokenType();
            if (type == TokenType.NUMBER || type == TokenType.VARIABLE) {
                postfix.add(token);
            } else if (type == TokenType.LEFT_PAREN) {
                operators.push(token);
            } else if (type == TokenType.RIGHT_PAREN) {
                while (operators.peek().getTokenType() != TokenType.LEFT_PAREN) {
                    postfix.add(operators.pop());
                }
                operators.pop();
            } else {
                while (!operators.empty()
                        && precedence(operators.peek().getTokenType()) >= precedence(type)) {
                    postfix.add(operators.pop());
                }
                operators.push(token);
            }
        }
        while (!operators.empty()) {
            postfix.add(operators.pop());
        }
        return postfix;
    }

    /**
     * Метод, который парсит строку с математическим выражением.
     * Принцип работы:
     * Вначале лексер разбивает всю строку на токены.
     * Затем по ним записывается Обратная Польская Запись, с использованием приоритета операторов.
     * И затем по ОПЗ с помощью стека вычисляется само выражение - объект класса Expression.
     *
     * @param expression строковое представление выражения
     * @return объект класса Expression, построенное по строковому представлению выражения.
     */
    public static Expression parse(String expression) {
        List <Token> allTokens = tokenize(expression);
        List <Token> postfixTokens = toPostfix(allTokens);
        Stack <Expression> allExpressions = new Stack <>();
        for (Token currentToken : postfixTokens) {
            TokenType type = currentToken.getTokenType();
            switch (type) {
                case NUMBER:
                    Expression exNumber = new Number(Integer.parseInt(currentToken.getValue()));
                    allExpressions.push(exNumber);
                    break;
                case VARIABLE:
                    Expression exVariable = new Variable(currentToken.getValue());
                    allExpressions.push(exVariable);
                    break;
                case PLUS:
                case MINUS:
                case MULTIPLY:
                case DIVIDE: {
                    Expression rightSide = allExpressions.pop();
                    Expression leftSide = allExpressions.pop();
                    switch (type) {
                        case PLUS:
                            allExpressions.push(new Add(leftSide, rightSide));
                            break;
                        case MINUS:
                            allExpressions.push(new Sub(leftSide, rightSide));
                            break;
                        case MULTIPLY:
                            allExpressions.push(new Mul(leftSide, rightSide));
                            break;
                        case DIVIDE:
                            allExpressions.push(new Div(leftSide, rightSide));
                            break;
                    }
                    break;
                }
                case UNARY_MINUS:
                    Expression lastExpression = allExpressions.pop();
                    if (lastExpression instanceof Number) {
                        ((Number) lastExpression).numberValue =
                                -((Number) lastExpression).numberValue;
                        allExpressions.push(lastExpression);
                    } else {
                        Expression result = new Sub(new Number(0), lastExpression);
                        allExpressions.push(result);
                    }
                    break;
            }
        }
        return allExpressions.pop();
    }
}