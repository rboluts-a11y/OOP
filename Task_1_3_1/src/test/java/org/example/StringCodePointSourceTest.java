package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.example.source.StringCodePointSource;
import org.junit.jupiter.api.Test;

class StringCodePointSourceTest {
    @Test
    void handlesEmptyString() {
        StringCodePointSource source = new StringCodePointSource("");
        assertEquals(-1, source.nextCodePoint());
    }

    @Test
    void handlesSingleEnglishLetter() {
        StringCodePointSource source = new StringCodePointSource("f");
        assertEquals('f', source.nextCodePoint());
        assertEquals(-1, source.nextCodePoint());
    }

    @Test
    void handlesAsciiCharacters() {
        StringCodePointSource source = new StringCodePointSource("AbC123!?");
        assertEquals('A', source.nextCodePoint());
        assertEquals('b', source.nextCodePoint());
        assertEquals('C', source.nextCodePoint());
        assertEquals('1', source.nextCodePoint());
        assertEquals('2', source.nextCodePoint());
        assertEquals('3', source.nextCodePoint());
        assertEquals('!', source.nextCodePoint());
        assertEquals('?', source.nextCodePoint());
        assertEquals(-1, source.nextCodePoint());
    }

    @Test
    void handlesRussianCharacters() {
        StringCodePointSource source = new StringCodePointSource("Привет");
        assertEquals('П', source.nextCodePoint());
        assertEquals('р', source.nextCodePoint());
        assertEquals('и', source.nextCodePoint());
        assertEquals('в', source.nextCodePoint());
        assertEquals('е', source.nextCodePoint());
        assertEquals('т', source.nextCodePoint());
        assertEquals(-1, source.nextCodePoint());
    }

    @Test
    void handlesChineseCharacters() {
        StringCodePointSource source = new StringCodePointSource("你好世界");
        assertEquals('你', source.nextCodePoint());
        assertEquals('好', source.nextCodePoint());
        assertEquals('世', source.nextCodePoint());
        assertEquals('界', source.nextCodePoint());
        assertEquals(-1, source.nextCodePoint());
    }

    @Test
    void handlesEmoji() {
        StringCodePointSource source = new StringCodePointSource("🐒🚀");
        assertEquals(0x1F412, source.nextCodePoint());
        assertEquals(0x1F680, source.nextCodePoint());
        assertEquals(-1, source.nextCodePoint());
    }

    @Test
    void handlesMixedCharacters() {
        StringCodePointSource source = new StringCodePointSource("sПривет🐒D你好🚀");
        assertEquals('s', source.nextCodePoint());
        assertEquals('П', source.nextCodePoint());
        assertEquals('р', source.nextCodePoint());
        assertEquals('и', source.nextCodePoint());
        assertEquals('в', source.nextCodePoint());
        assertEquals('е', source.nextCodePoint());
        assertEquals('т', source.nextCodePoint());
        assertEquals(0x1F412, source.nextCodePoint());
        assertEquals('D', source.nextCodePoint());
        assertEquals('你', source.nextCodePoint());
        assertEquals('好', source.nextCodePoint());
        assertEquals(0x1F680, source.nextCodePoint());
        assertEquals(-1, source.nextCodePoint());
    }
}