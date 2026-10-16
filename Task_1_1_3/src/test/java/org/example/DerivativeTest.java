package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DerivativeTest {
    @Test
    void numberTest() {
        Expression ex = new Number(29);
        Expression exDer = ex.derivative("x");
        assertEquals("0", exDer.toString());
    }

    @Test
    void variableTest() {
        Expression ex = new Variable("x");
        Expression exDerByX = ex.derivative("x");
        assertEquals("1", exDerByX.toString());
        Expression exDerByY = ex.derivative("y");
        assertEquals("0", exDerByY.toString());
    }

    @Test
    void oneBinaryOperationTest() {
        Expression exPlus = new Add(new Variable("x"), new Number(47));
        Expression exPlusDer = exPlus.derivative("x");
        assertEquals("(1+0)", exPlusDer.toString());

        Expression exMinus = new Sub(new Number(32), new Variable("x"));
        Expression exMinusDer = exMinus.derivative("x");
        assertEquals("(0-1)", exMinusDer.toString());

        Expression exMul = new Mul(new Variable("x"), new Variable("y"));
        Expression exMulDer = exMul.derivative("x");
        assertEquals("((1*y)+(x*0))", exMulDer.toString());

        Expression exDiv = new Div(new Number(1), new Variable("x"));
        Expression exDivDer = exDiv.derivative("x");
        assertEquals("(((0*x)-(1*1))/(x*x))", exDivDer.toString());
    }
}