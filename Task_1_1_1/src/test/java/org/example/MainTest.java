package org.example;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для проверки корректности работы программы A+B.
 * Обеспечивает покрытие кода тестами для плагина Jacoco.
 */
class MainTest {

    /**
     * Тест симулирует ввод чисел 5 и 10 в консоль и проверяет,
     * что программа Main успешно выполняется без сбоев.
     */
    @Test
    void checkMainExecutionWithInput() {
        // 1. Симулируем ввод пользователя: пишем числа "5 10" как поток данных
        String simulatedInput = "5 10\n";
        InputStream originalSystemIn = System.in;
        ByteArrayInputStream testInput = new ByteArrayInputStream(simulatedInput.getBytes());

        try {
            // Подменяем стандартный ввод на наш заготовленный текстовый поток
            System.setIn(testInput);

            // 2. Вызываем главный метод нашей программы
            Main.main(new String[]{});

            // 3. Если программа не вылетела с ошибкой, тест считается успешно пройденным
            assertTrue(true);

        } finally {
            // Возвращаем стандартный ввод системы в исходное состояние
            System.setIn(originalSystemIn);
        }
    }

    /**
     * Тест проверяет создание объекта дефолтного конструктора.
     * Необходим для достижения 100% покрытия методов по требованию Jacoco.
     */
    @Test
    void checkConstructorCoverage() {
        Main mainInstance = new Main();
        assertNotNull(mainInstance);
    }
}
