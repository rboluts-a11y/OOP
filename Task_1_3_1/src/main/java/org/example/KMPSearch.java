package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.example.source.CodePointSource;
import org.example.source.FileCodePointSource;
import org.example.source.StringCodePointSource;

/**
 * Реализует поиск всех вхождений подстроки в последовательности Unicode-кодовых точек
 * с использованием алгоритма Кнута — Морриса — Пратта (КМП) и префикс-функции.
 */
public class KMPSearch {
    private KMPSearch() {}

    /**
     * Находит все вхождения заданной подстроки в источнике кодовых точек.
     *
     * @param source источник кодовых точек
     * @param pattern искомая подстрока
     * @return список индексов начала всех найденных вхождений
     * @throws IOException если произошла ошибка чтения из источника
     * @throws IllegalArgumentException если искомая подстрока пуста
     */
    public static List<Long> find(CodePointSource source, String pattern) throws IOException {
        if (pattern.isEmpty()) {
            throw new IllegalArgumentException("Строка-паттерн не может быть пустой!");
        }
        final List<Long> allIndices = new ArrayList<>();
        int[] patternCodePoints = pattern.codePoints().toArray();
        int patternLength = patternCodePoints.length;
        int[] prefixFunctionForPattern = new int[patternLength];
        for (int i = 1; i < patternLength; i++) {
            int current = prefixFunctionForPattern[i - 1];
            while (patternCodePoints[i] != patternCodePoints[current] && current > 0) {
                current = prefixFunctionForPattern[current - 1];
            }
            if (patternCodePoints[i] == patternCodePoints[current]) {
                prefixFunctionForPattern[i] = current + 1;
            }
        }
        int lastValuePrefixFunction = 0;
        long indexSource = -1;
        int currentCodePoint;
        while ((currentCodePoint = source.nextCodePoint()) != -1) {
            indexSource++;
            int current = lastValuePrefixFunction;
            while (current == patternLength ||
                    (currentCodePoint != patternCodePoints[current] && current > 0)) {
                current = prefixFunctionForPattern[current - 1];
            }
            if (currentCodePoint == patternCodePoints[current]) {
                lastValuePrefixFunction = current + 1;
            } else {
                lastValuePrefixFunction = 0;
            }
            if (lastValuePrefixFunction == patternLength) {
                allIndices.add(indexSource - patternLength + 1);
            }
        }
        return allIndices;
    }
    /**
     * Находит все вхождения подстроки в файле.
     * Файл считывается последовательно в кодировке UTF-8.
     *
     * @param filename путь к файлу
     * @param pattern искомая подстрока
     * @return список индексов начала всех найденных вхождений
     * @throws IOException если произошла ошибка открытия или чтения файла
     * @throws IllegalArgumentException если искомая подстрока пуста
     */
    public static List<Long> find(String filename, String pattern) throws IOException {
        try (CodePointSource source = new FileCodePointSource(filename)) {
            return find(source, pattern);
        }
    }

    /**
     * Находит все вхождения подстроки в заданной строке.
     *
     * @param text исходная строка
     * @param pattern искомая подстрока
     * @return список индексов начала всех найденных вхождений
     * @throws IllegalArgumentException если искомая подстрока пуста
     */
    public static List<Long> findFromString(String text, String pattern) throws IOException {
        try (CodePointSource source = new StringCodePointSource(text)) {
            return find(source, pattern);
        }
    }
}
