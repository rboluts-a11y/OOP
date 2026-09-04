package org.example;

import java.util.Scanner;

/**
 * Дефолтный конструктор класса Main.
 */
public class Main {

    /**
     * Главный метод программы. Считывает два числа и выводит их сумму.
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        // Создаем объект для чтения ввода (аналог cin)
        Scanner scanner = new Scanner(System.in);

        // Читаем два целых числа
        int a = scanner.nextInt();
        int b = scanner.nextInt();

        // Считаем сумму
        int sum = a + b;

        // Выводим результат (аналог cout)
        System.out.println(sum);
    }
}
