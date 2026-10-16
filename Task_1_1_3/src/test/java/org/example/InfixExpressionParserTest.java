package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class InfixExpressionParserTest {
    @Test
    void onlyOneNumberTests() {
        Expression exNonNegative = InfixExpressionParser.parse("67");
        assertEquals("67", exNonNegative.toString());

        Expression exNegative = InfixExpressionParser.parse("-67");
        assertEquals("-67", exNegative.toString());
    }

    @Test
    void onlyOneVariableTest() {
        Expression expression = InfixExpressionParser.parse("x");
        assertEquals("x", expression.toString());
    }

    @Test
    void unaryMinusTests() {
        Expression negVariable = InfixExpressionParser.parse("-x");
        assertEquals("(0-x)", negVariable.toString());

        Expression multiplyFromNeg = InfixExpressionParser.parse("-5 * 2");
        assertEquals("(-5*2)", multiplyFromNeg.toString());

        Expression multiplyToNeg = InfixExpressionParser.parse("5*-1");
        assertEquals("(5*-1)", multiplyToNeg.toString());

        Expression unaryMinusAfterBinary = InfixExpressionParser.parse("2--1");
        assertEquals("(2--1)", unaryMinusAfterBinary.toString());

        Expression negExpression = InfixExpressionParser.parse("-(5 * 2)");
        assertEquals("(0-(5*2))", negExpression.toString());

        Expression minusAfterParen = InfixExpressionParser.parse("(-42+5)");
        assertEquals("(-42+5)", minusAfterParen.toString());
    }

    @Test
    void priorityOperationsTests() {
        Expression higherAfter = InfixExpressionParser.parse("2 + 3 * 4");
        assertEquals("(2+(3*4))", higherAfter.toString());

        Expression lowerAfter = InfixExpressionParser.parse("2 * 3 + 4");
        assertEquals("((2*3)+4)", lowerAfter.toString());

        Expression sameHighPrecedence = InfixExpressionParser.parse("3 * 4 / six");
        assertEquals("((3*4)/six)", sameHighPrecedence.toString());

        Expression sameLowPrecedence = InfixExpressionParser.parse("x-y-z");
        assertEquals("((x-y)-z)", sameLowPrecedence.toString());
    }

    @Test
    void bigNumberOfParensTest() {
        Expression overheadExpression = InfixExpressionParser.parse("(((((-5)))))");
        assertEquals("-5", overheadExpression.toString());
    }
}