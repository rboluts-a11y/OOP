package org.example;

import java.io.IOException;
import java.util.List;

/**
 * Демонстрирует поиск всех вхождений подстроки в текстовом файле.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        List<Long> allIndices = KMPSearch.find("src/main/resources/example.txt", "中🐒");
        System.out.println(allIndices);
    }
}