package org.example;

/**
 * Класс для токена - синтаксической единицы для лексера.
 * Содержит enum-тип токена и значение в строковом виде, если токен - это число или переменная.
 */
public class Token {
    private final TokenType type;
    private final String value;

    /**
     * Конструктор для класса Token.
     *
     * @param type enum-тип токена
     * @param value значение, если тип - число или переменная, в ином случае value = null
     */
    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Возвращает enum-тип токена.
     *
     * @return тип токена.
     */
    TokenType getTokenType() {
        return type;
    }

    /**
     * Возвращает значение в строковом виде, применяется только если тип - число или переменная.
     *
     * @return значение в строковом виде
     */
    String getValue() {
        return value;
    }
}