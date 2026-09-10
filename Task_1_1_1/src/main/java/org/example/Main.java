package org.example;

import java.util.Scanner;

/**
 * Главный класс приложения для выполнения лабораторной работы Task_1_1_1.
 * Содержит алгоритм пирамидальной сортировки и демонстрационный ввод-вывод.
 */
public class Main {
    /**
     * Точка входа в программу. Считывает размер массива и её элементы из консоли,
     * запускает сортировку и выводит результат.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int sizeArray = scanner.nextInt();
        int [] array = new int [sizeArray];
        for (int i = 0; i < sizeArray; i++) {
            array[i] = scanner.nextInt();
        }
        int [] sortedArray = Heap.heapsort(array);
        for (int i = 0; i < sizeArray; i++) {
            System.out.print(sortedArray[i] + " ");
        }
        System.out.println();
    }
}