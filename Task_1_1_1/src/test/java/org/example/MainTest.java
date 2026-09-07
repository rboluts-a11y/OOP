package org.example;

import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    //Проверка пустого массива
    @Test
    void testEmptyArray() {
        int[] input = new int[0];
        int[] expected = new int[0];
        assertArrayEquals(expected, Main.heapsort(input));
    }
    //Проверка массива из одного элемента
    @Test
    void testSingleElement() {
        int[] input = {42};
        int[] expected = {42};
        assertArrayEquals(expected, Main.heapsort(input));
    }
    //Проверка уже отсортированного массива
    @Test
    void testAlreadySorted() {
        int[] input = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};
        assertArrayEquals(expected, Main.heapsort(input));
    }

    //Проверка массива обратно упорядоченного
    @Test
    void testReverseSorted() {
        int[] input = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};
        assertArrayEquals(expected, Main.heapsort(input));
    }
    //Проверка массива с хаотическим порядком, дупликатами и числами с разными знаками
    @Test
    void testSmallTest() {
        int[] input = {-1, 3, -1, 5, 3, 0, -5, 2};
        int[] expected = {-5, -1, -1, 0, 2, 3, 3, 5};
        assertArrayEquals(expected, Main.heapsort(input));
    }
    //Проверка на случайном, но огромном массиве, в частности для проверки асимптотики
    @Test
    void testLargeRandomArray() {
        int size = 1000000;
        int[] input = new int[size];
        int[] expected = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int value = random.nextInt();
            input[i] = value;
            expected[i] = value;
        }
        java.util.Arrays.sort(expected);
        assertArrayEquals(expected, Main.heapsort(input));
    }
    //Тест для покрытия дефолтного конструктора
    @Test
    void testMainConstructor() {
        Main mainInstance = new Main();
        assertNotNull(mainInstance);
    }
}
