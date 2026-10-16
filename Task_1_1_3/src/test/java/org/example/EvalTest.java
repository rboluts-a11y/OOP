package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EvalTest {
    @Test
    void sampleCaseTest() {
        Expression e = new Add(
                new Number(3),
                new Mul(
                        new Number(2),
                        new Variable("x")
                )
        );
        int result = e.eval("x = 10; y = 13");
        assertEquals(23, result);
    }

    @Test
    void allTypesInOneExpressionTest() {
        Expression e = new Add(
                new Sub(
                        new Variable("x"),
                        new Number(67)
                ),
                new Mul(
                        new Variable("yz"),
                        new Div(
                                new Number(52),
                                new Variable("cat")
                        )
                )
        );
        int result = e.eval("x = 12; yz = -4; cat = -13");
        assertEquals(-39, result);
    }

    @Test
    void noNeedToDefineVariables() {
        Expression e = new Mul(
                new Sub(
                        new Number(52),
                        new Number(42)
                ),
                new Add(
                        new Number(13),
                        new Div(
                                new Number(32),
                                new Number(16)
                        )
                )
        );
        int result = e.eval("");
        assertEquals(150, result);
    }
}