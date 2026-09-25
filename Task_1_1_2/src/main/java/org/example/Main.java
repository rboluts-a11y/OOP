package org.example;

import java.util.Objects;

/**
 * Главный класс приложения для выполнения лабораторной работы Task_1_1_2.
 * Содержит реализацию консольного Блэкджека с красивым выводом, удобным для человека.
 * Также реализована задержка по времени перед ходами дилера и выводом результата раунда.
 */
public class Main {
    /**
     * Точка входа в игру. Реализует бесконечный цикл раундов.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        Game game = new Game();
        System.out.println(game.hello());
        do {
            System.out.print(game.newRound());
            String cur1 = game.doIfAnyoneHasBlackjack();
            if (Objects.equals(cur1, "")) {
                game.processPlayerTakes();
                String cur2 = game.doIfPlayerIsBusted();
                if (Objects.equals(cur2, "")) {
                    System.out.print(game.processDealerTakes());
                    System.out.println(game.getRoundResult());
                } else {
                    System.out.println(cur2);
                }
            } else {
                System.out.println(cur1);
            }
            System.out.println(game.getScore());
        } while (game.isPlayerIfWantToContinue());
        System.out.println(game.goodbye());
    }
}