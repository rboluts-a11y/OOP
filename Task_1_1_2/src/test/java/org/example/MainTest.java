package org.example;

import org.junit.jupiter.api.Test;

class MainTest {
    @Test
    void testWait() {
        Main.waitSeconds(0.001);
        Main.waitSeconds(0.01);
        Main.waitSeconds(0.1);
        Main.waitSeconds(1);
    }
}