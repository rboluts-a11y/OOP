package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ToStringTest {
    @Test
    void allTypesInOneExpressionTest() {
        Expression e = new Add(
                new Sub (
                        new Variable("x"),
                        new Number(67)
                ),
                new Mul (
                        new Variable("yz"),
                        new Div(
                                new Number(52),
                                new Variable("cat")
                        )
                )
        );
        assertEquals(
                "((x-67)+(yz*(52/cat)))",
                e.toString()
        );
    }

    @Test
    void oneNumberTests() {
        Expression e1 = new Number(42);
        assertEquals("42", e1.toString());

        Expression e2 = new Number(-2849);
        assertEquals("-2849", e2.toString());
    }

    @Test
    void oneVariableTests() {
        Expression e1 = new Variable("x");
        assertEquals("x", e1.toString());

        Expression e2 = new Variable("Meow");
        assertEquals("Meow", e2.toString());
    }
}