package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

class KMPSearchTest {
    @Test
    void findsOccurrences() throws IOException {
        List<Long> allIndices = KMPSearch.findFromString("abracadabra", "bra");
        assertEquals(List.of(1L, 8L), allIndices);
    }

    @Test
    void findsOverlappingOccurrences() throws IOException {
        List<Long> allIndices = KMPSearch.findFromString("aaaaaa", "aaa");
        assertEquals(List.of(0L, 1L, 2L, 3L), allIndices);
    }

    @Test
    void findsOverlappingOccurrencesWithFallback() throws IOException {
        List<Long> allIndices = KMPSearch.findFromString("abababab", "abab");
        assertEquals(List.of(0L, 2L, 4L), allIndices);
    }

    @Test
    void returnsEmptyListWhenPatternIsLonger() throws IOException {
        List<Long> allIndices = KMPSearch.findFromString("abcabc", "abcd");
        assertEquals(List.of(), allIndices);
    }

    @Test
    void findsWholeText() throws IOException {
        List<Long> allIndices = KMPSearch.findFromString("abc", "abc");
        assertEquals(List.of(0L), allIndices);
    }

    @Test
    void returnsEmptyListWhenThereAreNoMatches() throws IOException {
        List<Long> allIndices = KMPSearch.findFromString("abc", "z");
        assertEquals(List.of(), allIndices);
    }

    @Test
    void fallsBackUsingPrefixFunction() throws IOException {
        List<Long> allIndices = KMPSearch.findFromString("ABABAC", "ABABAC");
        assertEquals(List.of(0L), allIndices);
    }

    @Test
    void handlesEmptyText() throws IOException {
        List<Long> allIndices = KMPSearch.findFromString("", "a");
        assertEquals(List.of(), allIndices);
    }

    @Test
    void handlesEmoji() throws IOException {
        List<Long> allIndices = KMPSearch.findFromString("A🐒Б🐒", "🐒");
        assertEquals(List.of(1L, 3L), allIndices);
    }

    @Test
    void handlesChineseCharacters() throws IOException {
        List<Long> allIndices = KMPSearch.findFromString("你好世界你好", "你好");
        assertEquals(List.of(0L, 4L), allIndices);
    }

    @Test
    void handlesOverlappingChineseCharacters() throws IOException {
        List<Long> allIndices = KMPSearch.findFromString("你好你好你好", "你好你好");
        assertEquals(List.of(0L, 2L), allIndices);
    }

    @Test
    void throwsExceptionForEmptyPattern() {
        assertThrows(
                IllegalArgumentException.class,
                () -> KMPSearch.findFromString("abc", "")
        );
    }
}
