package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ExpressionParserTest {
    @Test
    void parseNumberTest() {
        Expression e = ExpressionParser.parse("42");
        assertEquals("42", e.toString());
    }

    @Test
    void parseNegativeNumberTest() {
        Expression e = ExpressionParser.parse("-3");
        assertEquals("-3", e.toString());
    }

    @Test
    void parseVariableTest() {
        Expression e = ExpressionParser.parse("Meow");
        assertEquals("Meow", e.toString());
    }

    @Test
    void parseComplexExpressionTest() {
        Expression e = ExpressionParser.parse("((-12+x)/((Meow*5)-y))");
        assertEquals("((-12+x)/((Meow*5)-y))", e.toString());
    }

    @Test
    void parseUnaryMinus() {
        Expression e = ExpressionParser.parse("(-5*x)");
        assertEquals("(-5*x)", e.toString());
    }
}