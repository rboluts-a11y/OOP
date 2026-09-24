package org.example;

import java.util.Scanner;

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
        System.setOut(new java.io.PrintStream(System.out,
                true, java.nio.charset.StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(System.in, java.nio.charset.StandardCharsets.UTF_8);

        ScoreState scoreState = new ScoreState();
        RoundState roundState = new RoundState();

        System.out.println("Добро пожаловать в Блэкджек!");

        int isPlayerWantToPlay = 1;
        while (isPlayerWantToPlay == 1) {
            System.out.println(scoreState.addNewGame());

            roundState.newRound();

            System.out.println("Дилер раздал карты!\n" + roundState.getStateInformation());

            boolean hasPlayerBlackjack = roundState.isPlayerHasBlackjack();
            boolean hasDealerBlackjack = roundState.isDealerHasBlackjack();
            if (hasPlayerBlackjack && hasDealerBlackjack) {
                System.out.println(roundState.dealerOpenSecondCard());
                System.out.print(scoreState.drawPush("У вас и у дилера Блэкджек со старта! "));
            } else if (hasPlayerBlackjack)  {
                System.out.println(roundState.dealerOpenSecondCard());
                System.out.print(scoreState.playerWon("У вас Блэкджек со старта!"));
            } else if (hasDealerBlackjack) {
                System.out.println(roundState.dealerOpenSecondCard());
                System.out.print(scoreState.dealerWon("У дилера Блэкджек со старта!"));
            } else {
                System.out.println("Ваш ход");
                System.out.println("-------");

                while (!roundState.isPlayerBusted()) {
                    String msg = "Введите “1”, чтобы взять карту, и “0”, чтобы остановиться... ";
                    System.out.println(msg);
                    int choice = scanner.nextInt();
                    if (choice == 0) {
                        break;
                    }
                    if (choice == 1) {
                        System.out.println(roundState.playerTakeCard());
                        System.out.println(roundState.getStateInformation());
                    }
                }

                if (roundState.isPlayerBusted()) {
                    System.out.print(scoreState.dealerWon("У вас перебор!"));
                } else {
                    System.out.println("\nХод дилера");
                    System.out.println("-------");

                    waitSeconds(1.0);

                    System.out.println(roundState.dealerOpenSecondCard());
                    System.out.println(roundState.getStateInformation());

                    while (roundState.isDealerMustToMove()) {
                        waitSeconds(1.5);
                        System.out.println(roundState.dealerTakeCard());
                        System.out.println(roundState.getStateInformation());
                    }

                    waitSeconds(1.0);

                    if (roundState.isDealerBusted()) {
                        System.out.print(scoreState.playerWon("У дилера перебор!"));
                    } else {
                        int whoWon = roundState.calculateWhoWonByScore();
                        if (whoWon > 0) {
                            System.out.print(scoreState.playerWon("Вы набрали больше очков!"));
                        } else if (whoWon == 0) {
                            System.out.print(scoreState.drawPush(""));
                        } else {
                            System.out.print(scoreState.dealerWon("Дилер набрал больше очков."));
                        }
                    }
                }
            }

            System.out.println(scoreState.getScoreNotification());

            System.out.println("\nВведите “1”, если хотите сыграть ещё, и “0”, чтобы остановиться... ");
            isPlayerWantToPlay = scanner.nextInt();
            System.out.println();
        }

        System.out.println("Спасибо за игру! Мы будем рады видеть Вас ещё!");
    }

    /**
     * Ставит программу на паузу на указанное количество секунд.
     * Обернуто в try-catch для безопасной обработки InterruptedException.
     *
     * @param seconds количество секунд для паузы
     */
    public static void waitSeconds(double seconds) {
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}