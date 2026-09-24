package org.example;

import java.util.Scanner;

/**
 * Класс для всей игры, содержит состояние всей игры по раундам и текущий раунд.
 */
public class Game {
    private final RoundState roundState;
    private final ScoreState scoreState;
    private final Scanner scanner;

    /**
     *  Конструктор класса. Устанавливает начальное состояние игры.
     */
    public Game() {
        scoreState = new ScoreState();
        roundState = new RoundState();
        System.setOut(new java.io.PrintStream(System.out,
                true, java.nio.charset.StandardCharsets.UTF_8));
        scanner = new Scanner(System.in, java.nio.charset.StandardCharsets.UTF_8);
    }

    /**
     * Метод, который здоровается с пользователем.
     */
    public void hello() {
        System.out.println("Добро пожаловать в Блэкджек!");
    }

    /**
     * Метод, который начинает новый раунд и раздает карты.
     */
    public void newRound() {
        System.out.println(scoreState.addNewGame());
        roundState.newRound();
        System.out.println("Дилер раздал карты!\n" + roundState.getStateInformation());
    }

    /**
     * Проверяет, надо ли закончить игру в самом вначале по причине того что у кого-то Блэкджек.
     * Если да, то вычисляет результат игры и заканчивает игру.
     *
     * @return булевое значение, означающее, закончилась ли игра
     */
    public boolean doIfAnyoneHasBlackjack() {
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
        }
        return hasPlayerBlackjack || hasDealerBlackjack;
    }

    /**
     * Метод, который симулирует процесс взятия игроком карт до остановки.
     */
    public void processPlayerTakes() {
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
    }

    /**
     * Определяет, нужно ли закончить игру в связи с перебором игрока.
     *
     * @return соответствующее булевое значение.
     */
    public boolean doIfPlayerIsBusted() {
        if (roundState.isPlayerBusted()) {
            System.out.print(scoreState.dealerWon("У вас перебор!"));
            return true;
        }
        return false;
    }

    /**
     * Метод, который симулирует процесс взятия дилером карт до остановки.
     */
    public void processDealerTakes() {
        System.out.println("\nХод дилера");
        System.out.println("-------");

        System.out.println(roundState.dealerOpenSecondCard());
        System.out.println(roundState.getStateInformation());

        while (roundState.isDealerMustToMove()) {
            System.out.println(roundState.dealerTakeCard());
            System.out.println(roundState.getStateInformation());
        }
    }

    /**
     * Метод, который определяет, кто выиграл и печатает соответствующее сообщение.
     */
    public void printRoundResult() {
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

    /**
     * Метод, который печатает результат.
     */
    public void printScore() {
        System.out.println(scoreState.getScoreNotification());
    }

    /**
     * Метод, который определяет у игрока, хочет ли он продолжать игру или нет.
     *
     * @return соответствующее булевое значение
     */
    public boolean isPlayerIfWantToContinue() {
        System.out.println("\nВведите “1”, если хотите сыграть ещё, и “0”, чтобы остановиться... ");
        int resultFromPlayer = scanner.nextInt();
        System.out.println();
        return resultFromPlayer == 1;
    }

    /**
     * Метод для прощания с пользователем.
     */
    public void goodbye() {
        System.out.println("Спасибо за игру! Мы будем рады видеть Вас ещё!");
    }
}