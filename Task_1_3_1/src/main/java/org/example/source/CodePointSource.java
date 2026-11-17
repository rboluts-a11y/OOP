package org.example.source;

import java.io.IOException;

/**
 * Источник Unicode-кодовых точек, позволяющий последовательно считывать их
 * без необходимости загружать весь текст в память.
 */
public interface CodePointSource extends AutoCloseable {
    /**
     * Считывает следующую кодовую точку.
     *
     * @return следующая кодовая точка или -1, если источник достиг конца
     * @throws IOException если произошла ошибка при чтении
     */
    int nextCodePoint() throws IOException;

    /**
     * Закрывает источник и освобождает связанные с ним ресурсы.
     *
     * @throws IOException если произошла ошибка при закрытии источника
     */
    @Override
    void close() throws IOException;
}
