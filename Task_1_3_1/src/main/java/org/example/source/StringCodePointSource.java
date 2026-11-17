package org.example.source;


/**
 * Источник кодовых точек, последовательно считываемых из строки.
 */
public class StringCodePointSource implements CodePointSource {
    private final String text;
    private int index = 0;

    /**
     * Создаёт источник на основе переданной строки.
     *
     * @param text строка, из которой будут считываться кодовые точки
     */
    public StringCodePointSource(String text) {
        this.text = text;
    }

    /**
     * Считывает следующую кодовую точку строки.
     * Учитывает, что символы за пределами базовой многоязычной плоскости
     * Unicode представлены в Java парой суррогатных символов UTF-16.
     *
     * @return следующая кодовая точка или -1, если достигнут конец строки
     */
    @Override
    public int nextCodePoint() {
        if (index >= text.length()) {
            return -1;
        }
        int codePoint = text.codePointAt(index);
        index += Character.charCount(codePoint);
        return codePoint;
    }

    /**
     * Закрывает источник.
     * Дополнительные действия не требуются, поскольку строка не задействует внешние ресурсы.
     */
    @Override
    public void close() {
    }
}