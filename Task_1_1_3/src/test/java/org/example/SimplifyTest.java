package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SimplifyTest {
    @Test
    void noVariablesTest() {
        Expression noVariables = new Div(
                new Add(
                        new Number(21),
                        new Mul(
                                new Number(12),
                                new Number(4)
                        )
                ),
                new Sub(
                        new Number(32),
                        new Number(9)
                )
        );
        Expression simplified = noVariables.simplify();
        assertEquals("3", simplified.toString());
    }

    @Test
    void subEqualExpressionsTest() {
        Expression subEqualExpressions = ExpressionParser.parse("((14*(xy-2))-(14*(xy-2)))");
        Expression simplified = subEqualExpressions.simplify();
        assertEquals("0", simplified.toString());
    }

    @Test
    void multiplyByZeroTests() {
        Expression leftSideIsZero = ExpressionParser.parse("((6-(3*2))*(13/(14+(Xx-(52/s)))))");
        Expression simplifiedFirst = leftSideIsZero.simplify();
        assertEquals("0", simplifiedFirst.toString());

        Expression rightSideIsZero = ExpressionParser.parse("((xyn-(pen*7))*0)");
        Expression simplifiedSecond = rightSideIsZero.simplify();
        assertEquals("0", simplifiedSecond.toString());
    }

    @Test
    void multiplyByOneTests() {
        Expression leftSideIsZero = ExpressionParser.parse("(1*(2/(11+(X+(52/s)))))");
        Expression simplifiedFirst = leftSideIsZero.simplify();
        assertEquals("(2/(11+(X+(52/s))))", simplifiedFirst.toString());

        Expression rightSideIsZero = ExpressionParser.parse("((12-((32*x)/y))*(10-(3*3)))");
        Expression simplifiedSecond = rightSideIsZero.simplify();
        assertEquals("(12-((32*x)/y))", simplifiedSecond.toString());
    }

    @Test
    void someComplexTest() {
        Expression expression = ExpressionParser.parse("(1/x)");
        Expression exDer = expression.derivative("x");
        Expression simplified = exDer.simplify();
        assertEquals("(-1/(x*x))", simplified.toString());
    }
}