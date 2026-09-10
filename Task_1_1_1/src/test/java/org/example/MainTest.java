package org.example;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class MainTest {
    //Проверяет функцию main, симулируя ввод пользователя
    @Test
    void testMainMethodWithInputOutput() {
        String simulatedInput = "5\n5 4 3 2 1\n";
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        try {
            System.setIn(testIn);
            System.setOut(new PrintStream(testOut));
            Main.main(new String[]{});
            String actualOutput = testOut.toString().trim();
            String expectedOutput = "1 2 3 4 5";
            assertEquals(expectedOutput, actualOutput);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    //Проверка пустого массива
    @Test
    void testEmptyArray() {
        int[] input = new int[0];
        int[] expected = new int[0];
        assertArrayEquals(expected, Heap.heapsort(input));
    }

    //Проверка массива из одного элемента
    @Test
    void testSingleElement() {
        int[] input = {42};
        int[] expected = {42};
        assertArrayEquals(expected, Heap.heapsort(input));
    }

    //Проверка уже отсортированного массива
    @Test
    void testAlreadySorted() {
        int[] input = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};
        assertArrayEquals(expected, Heap.heapsort(input));
    }

    //Проверка массива обратно упорядоченного
    @Test
    void testReverseSorted() {
        int[] input = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};
        assertArrayEquals(expected, Heap.heapsort(input));
    }

    //Проверка массива с хаотическим порядком, дупликатами и числами с разными знаками
    @Test
    void testSmallTest() {
        int[] input = {-1, 3, -1, 5, 3, 0, -5, 2};
        int[] expected = {-5, -1, -1, 0, 2, 3, 3, 5};
        assertArrayEquals(expected, Heap.heapsort(input));
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
        Arrays.sort(expected);
        assertArrayEquals(expected, Heap.heapsort(input));
    }

    //Тест для покрытия дефолтного конструктора
    @Test
    void testMainConstructor() {
        Main mainInstance = new Main();
        assertNotNull(mainInstance);
    }

    //Тест проверяет изолированную работу структуры данных Heap
    @Test
    void testHeapStructureIsolately() {
        Heap testHeap = new Heap(5);
        testHeap.addNewElement(5);
        testHeap.addNewElement(3);
        testHeap.addNewElement(8);
        testHeap.addNewElement(3);
        testHeap.addNewElement(1);
        assertEquals(1, testHeap.extractMinElement());
        assertEquals(3, testHeap.extractMinElement());
        assertEquals(3, testHeap.extractMinElement());
        testHeap.addNewElement(7);
        assertEquals(5, testHeap.extractMinElement());
        assertEquals(7, testHeap.extractMinElement());
        assertEquals(8, testHeap.extractMinElement());
    }

    //Граничный тест. Проверяет, что пустая куча генерирует правильное исключение
    @Test
    void testEmptyHeapThrowsException() {
        Heap emptyHeap = new Heap(2);

        // Проверяем, что вызов метода на пустой куче выбросит IllegalStateException
        assertThrows(IllegalStateException.class, () -> {
            emptyHeap.extractMinElement();
        });
    }

    /*
     * Граничный тест. Проверяет, что при превышении максимальной емкости кучи
     * выбрасывается исключение IndexOutOfBoundsException.
     */
    @Test
    void testFullHeapThrowsException() {
        Heap fullHeap = new Heap(2);
        fullHeap.addNewElement(10);
        fullHeap.addNewElement(20);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            fullHeap.addNewElement(30);
        });
    }
}
