package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.example.source.FileCodePointSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileCodePointSourceTest {
    @TempDir
    Path tempDir;

    private Path createFile(String content) throws IOException {
        Path file = tempDir.resolve("input.txt");
        Files.writeString(file, content, StandardCharsets.UTF_8);
        return file;
    }

    @Test
    void handlesEmptyFile() throws IOException {
        Path file = createFile("");
        try (FileCodePointSource reader = new FileCodePointSource(file.toString())) {
            assertEquals(-1, reader.nextCodePoint());
        }
    }

    @Test
    void handlesSingleEnglishLetter() throws IOException {
        Path file = createFile("f");
        try (FileCodePointSource reader = new FileCodePointSource(file.toString())) {
            assertEquals('f', reader.nextCodePoint());
            assertEquals(-1, reader.nextCodePoint());
        }
    }

    @Test
    void handlesAsciiCharacters() throws IOException {
        Path file = createFile("AbC123!?");
        try (FileCodePointSource reader = new FileCodePointSource(file.toString())) {
            assertEquals('A', reader.nextCodePoint());
            assertEquals('b', reader.nextCodePoint());
            assertEquals('C', reader.nextCodePoint());
            assertEquals('1', reader.nextCodePoint());
            assertEquals('2', reader.nextCodePoint());
            assertEquals('3', reader.nextCodePoint());
            assertEquals('!', reader.nextCodePoint());
            assertEquals('?', reader.nextCodePoint());
            assertEquals(-1, reader.nextCodePoint());
        }
    }

    @Test
    void handlesRussianCharacters() throws IOException {
        Path file = createFile("Привет");
        try (FileCodePointSource reader = new FileCodePointSource(file.toString())) {
            assertEquals('П', reader.nextCodePoint());
            assertEquals('р', reader.nextCodePoint());
            assertEquals('и', reader.nextCodePoint());
            assertEquals('в', reader.nextCodePoint());
            assertEquals('е', reader.nextCodePoint());
            assertEquals('т', reader.nextCodePoint());
            assertEquals(-1, reader.nextCodePoint());
        }
    }

    @Test
    void handlesChineseCharacters() throws IOException {
        Path file = createFile("你好世界");
        try (FileCodePointSource reader = new FileCodePointSource(file.toString())) {
            assertEquals('你', reader.nextCodePoint());
            assertEquals('好', reader.nextCodePoint());
            assertEquals('世', reader.nextCodePoint());
            assertEquals('界', reader.nextCodePoint());
            assertEquals(-1, reader.nextCodePoint());
        }
    }

    @Test
    void handlesEmoji() throws IOException {
        Path file = createFile("🐒🚀");
        try (FileCodePointSource reader = new FileCodePointSource(file.toString())) {
            assertEquals(0x1F412, reader.nextCodePoint());
            assertEquals(0x1F680, reader.nextCodePoint());
            assertEquals(-1, reader.nextCodePoint());
        }
    }

    @Test
    void handlesMixedCharacters() throws IOException {
        Path file = createFile("AПривет🐒B你好🚀");
        try (FileCodePointSource reader = new FileCodePointSource(file.toString())) {
            assertEquals('A', reader.nextCodePoint());
            assertEquals('П', reader.nextCodePoint());
            assertEquals('р', reader.nextCodePoint());
            assertEquals('и', reader.nextCodePoint());
            assertEquals('в', reader.nextCodePoint());
            assertEquals('е', reader.nextCodePoint());
            assertEquals('т', reader.nextCodePoint());
            assertEquals(0x1F412, reader.nextCodePoint());
            assertEquals('B', reader.nextCodePoint());
            assertEquals('你', reader.nextCodePoint());
            assertEquals('好', reader.nextCodePoint());
            assertEquals(0x1F680, reader.nextCodePoint());
            assertEquals(-1, reader.nextCodePoint());
        }
    }

    @Test
    void readsFromExistingFile() throws IOException {
        Path file = Path.of("src", "test", "resources", "unicode.txt");
        try (FileCodePointSource reader = new FileCodePointSource(file.toString())) {
            assertEquals('H', reader.nextCodePoint());
            assertEquals('e', reader.nextCodePoint());
            assertEquals('l', reader.nextCodePoint());
            assertEquals('l', reader.nextCodePoint());
            assertEquals('o', reader.nextCodePoint());
            assertEquals(' ', reader.nextCodePoint());
            assertEquals('П', reader.nextCodePoint());
            assertEquals('р', reader.nextCodePoint());
            assertEquals('и', reader.nextCodePoint());
            assertEquals('в', reader.nextCodePoint());
            assertEquals('е', reader.nextCodePoint());
            assertEquals('т', reader.nextCodePoint());
            assertEquals('!', reader.nextCodePoint());
            assertEquals(' ', reader.nextCodePoint());
            assertEquals('你', reader.nextCodePoint());
            assertEquals('好', reader.nextCodePoint());
            assertEquals(' ', reader.nextCodePoint());
            assertEquals(0x1F412, reader.nextCodePoint());
            assertEquals(0x1F680, reader.nextCodePoint());
            assertEquals(-1, reader.nextCodePoint());
        }
    }
}