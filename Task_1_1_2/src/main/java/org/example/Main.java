package org.example;

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
        game.hello();
        do {
            game.newRound();
            if (!game.doIfAnyoneHasBlackjack()) {
                game.processPlayerTakes();
                if (!game.doIfPlayerIsBusted()) {
                    game.processDealerTakes();
                    game.printRoundResult();
                }
            }
            game.printScore();
        } while (game.isPlayerIfWantToContinue());
        game.goodbye();
    }
}