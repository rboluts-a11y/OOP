package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class HugeStringTest {
    @TempDir
    Path tempDir;

    @Test
    void loadingHugeFileIntoMemoryCausesOutOfMemory() throws IOException {
        Path file = tempDir.resolve("large.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            for (int i = 0; i < 100_000_000; i++) {
                writer.write('a');
            }
        }
        assertThrows(OutOfMemoryError.class, () ->
                Files.readString(file, StandardCharsets.UTF_8)
        );
    }

    @Test
    void findsPatternNearEndOfHugeFile() throws IOException {
        Path file = tempDir.resolve("huge.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            for (int i = 0; i < 100_000_000; i++) {
                writer.write('a');
            }
            writer.write("needle");
        }
        List<Long> result = KMPSearch.find(file.toString(), "needle");
        assertEquals(List.of(100_000_000L), result);
    }
}

