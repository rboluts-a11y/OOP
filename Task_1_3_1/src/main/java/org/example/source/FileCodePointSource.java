package org.example.source;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Источник кодовых точек, считываемых из файла в кодировке UTF-8.
 * Чтение выполняется последовательно, без загрузки всего файла в память.
 */
public class FileCodePointSource implements CodePointSource {
    private final BufferedReader reader;

    /**
     * Открывает файл для чтения в кодировке UTF-8.
     *
     * @param filename путь к файлу
     * @throws IOException если не удалось открыть файл
     */
    public FileCodePointSource(String filename) throws IOException {
        this.reader = Files.newBufferedReader(
                Path.of(filename),
                StandardCharsets.UTF_8
        );
    }

    /**
     * Считывает следующую кодовую точку из файла.
     * При необходимости объединяет пару суррогатных символов UTF-16 в одну кодовую точку Unicode.
     *
     * @return следующая кодовая точка или -1, если достигнут конец файла
     * @throws IOException если произошла ошибка чтения или обнаружена некорректная
     * последовательность суррогатных символов
     */
    public int nextCodePoint() throws IOException {
        int first = reader.read();
        if (first == -1) {
            return -1;
        }
        char high = (char) first;
        if (!Character.isHighSurrogate(high)) {
            return first;
        }
        int second = reader.read();
        if (second == -1) {
            throw new IOException("Неожиданный конец файла после старшего суррогата");
        }
        char low = (char) second;
        if (!Character.isLowSurrogate(low)) {
            throw new IOException("Некорректная пара суррогатных символов");
        }
        return Character.toCodePoint(high, low);
    }

    /**
     * Закрывает файл и освобождает связанные с ним ресурсы.
     *
     * @throws IOException если произошла ошибка при закрытии файла
     */
    @Override
    public void close() throws IOException {
        reader.close();
    }
}