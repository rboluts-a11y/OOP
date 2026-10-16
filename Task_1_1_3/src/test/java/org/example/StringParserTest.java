package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.Test;

class StringParserTest {
    @Test
    void sampleCaseTest() {
        Map<String, Integer> result = StringParser.parse("x = 10; y = 13");
        Map<String, Integer> expected = Map.of("x", 10, "y", 13);
        assertEquals(expected, result);
    }

    @Test
    void oneVariableTest() {
        Map<String, Integer> result = StringParser.parse("x = 1");
        Map<String, Integer> expected = Map.of("x", 1);
        assertEquals(expected, result);
    }

    @Test
    void negativeNumbersTest() {
        Map<String, Integer> result = StringParser.parse("x = -52; y = -67");
        Map<String, Integer> expected = Map.of("x", -52, "y", -67);
        assertEquals(expected, result);
    }

    @Test
    void longNamesVariablesTest() {
        Map<String, Integer> result = StringParser.parse("cat = 1; Meow = 2");
        Map<String, Integer> expected = Map.of("cat", 1, "Meow", 2);
        assertEquals(expected, result);
    }

}