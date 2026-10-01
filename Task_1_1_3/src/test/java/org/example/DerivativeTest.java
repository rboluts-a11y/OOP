package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DerivativeTest {
    @Test
    void numberTest() {
        Expression e = new Number(29);
        Expression eDer = e.derivative("x");
        assertEquals("0", eDer.toString());
    }

    @Test
    void variableTest() {
        Expression e = new Variable("x");
        Expression eDerByX = e.derivative("x");
        assertEquals("1", eDerByX.toString());
        Expression eDerByY = e.derivative("y");
        assertEquals("0", eDerByY.toString());
    }

    @Test
    void oneBinaryOperationTest() {
        Expression ePlus = new Add(new Variable("x"), new Number(47));
        Expression ePlusDer = ePlus.derivative("x");
        assertEquals("(1+0)", ePlusDer.toString());

        Expression eMinus = new Sub(new Number(32), new Variable("x"));
        Expression eMinusDer = eMinus.derivative("x");
        assertEquals("(0-1)", eMinusDer.toString());

        Expression eMul = new Mul(new Variable("x"), new Variable("y"));
        Expression eMulDer = eMul.derivative("x");
        assertEquals("((1*y)+(x*0))", eMulDer.toString());

        Expression eDiv = new Div(new Number(1), new Variable("x"));
        Expression eDivDer = eDiv.derivative("x");
        assertEquals("(((0*x)-(1*1))/(x*x))", eDivDer.toString());

    }
}