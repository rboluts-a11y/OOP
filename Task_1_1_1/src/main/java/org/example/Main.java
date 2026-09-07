package org.example;

import java.util.Scanner;

/**
 * Главный класс приложения для выполнения лабораторной работы Task_1_1_1.
 * Содержит алгоритм пирамидальной сортировки и демонстрационный ввод-вывод.
 */
public class Main {
    /**
     * Дефолтный конструктор класса Main.
     */
    public Main() {
    }
    /**
     * Метод принимает неотсортированный массив чисел типа int и возвращает новый, отсортированный по возрастанию.
     * Реализует алгоритм Heapsort с временной сложностью O(N log N).
     * @param givenArray исходный массив целых чисел
     * @return новый массив, элементы которого отсортированы по возрастанию
     */
    public static int [] heapsort(int [] givenArray) {
        int sizeArray = givenArray.length;
        Heap heapToSort = new Heap(sizeArray);
        int [] sortedArray = new int [sizeArray];
        for (int element : givenArray) {
            heapToSort.addNewElement(element);
        }
        for (int i = 0; i < sizeArray; i++) {
            sortedArray[i] = heapToSort.extractMinElement();
        }
        return sortedArray;
    }
    /**
     * Точка входа в программу. Считывает размер массива и её элементы из консоли,
     * запускает сортировку и выводит результат.
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int sizeArray = scanner.nextInt();
        int [] array = new int [sizeArray];
        for (int i = 0; i < sizeArray; i++) {
            array[i] = scanner.nextInt();
        }
        int [] sortedArray = heapsort(array);
        for (int i = 0; i < sizeArray; i++) {
            System.out.print(sortedArray[i] + " ");
        }
        System.out.println();
    }
}