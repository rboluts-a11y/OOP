package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class HugeStringTest {
    @TempDir
    Path tempDir;

    @Test
    void loadingHugeFileIntoMemoryCausesOutOfMemory() throws IOException {
        Path file = tempDir.resolve("huge.txt");
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

    @Test
    void findsRepeatedOccurrencesInHugeFile() throws IOException {
        Path file = tempDir.resolve("repeated.txt");
        String block = "The quick brown fox jumps over the lazy dog. ";
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            for (int i = 0; i < 1_000_000; i++) {
                writer.write(block);
            }
        }
        List<Long> result = KMPSearch.find(file.toString(), "brown fox");
        assertEquals(1_000_000, result.size());
        assertEquals(10L, result.get(0));
        assertEquals(55L, result.get(1));
        assertEquals(10L + 45L * 999_999, result.get(result.size() - 1));
    }

    @Test
    void findsRareOccurrencesInLargeLogFile() throws IOException {
        Path file = tempDir.resolve("large-log.txt");
        String normal = "2026-11-20 INFO  user=000001 action=login status=success\n";
        String error = "2026-11-20 ERROR user=000001 action=login status=failed\n";
        List<Long> expected = new ArrayList<>();
        long position = 0;
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            for (int i = 0; i < 1_000_000; i++) {
                String line = (i % 100_000 == 0) ? error : normal;
                if (i % 100_000 == 0) {
                    expected.add(position + "2026-11-20 ".length());
                }
                writer.write(line);
                position += line.length();
            }
        }
        List<Long> result = KMPSearch.find(file.toString(), "ERROR");
        assertEquals(expected, result);
    }

    @Test
    void handlesManyLongPartialMatches() throws IOException {
        Path file = tempDir.resolve("partial-matches.txt");
        String block = "aaaaaaaaac";
        String pattern = "aaaaaaaaab";
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            for (int i = 0; i < 10_000_000; i++) {
                writer.write(block);
            }
            writer.write(pattern);
        }
        List<Long> result = KMPSearch.find(file.toString(), pattern);
        assertEquals(List.of(100_000_000L), result);
    }
}