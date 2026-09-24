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
     *
     * @return возвращает строку приветствия
     */
    public String hello() {
         return "Добро пожаловать в Блэкджек!";
    }

    /**
     * Метод, который начинает новый раунд и раздает карты.
     */
    public String newRound() {
        StringBuilder sb = new StringBuilder();
        sb.append(scoreState.addNewGame()).append("\n");
        roundState.newRound();
        sb.append("Дилер раздал карты!\n").append(roundState.getStateInformation()).append("\n");
        return sb.toString();
    }

    /**
     * Проверяет, надо ли закончить игру в самом вначале по причине того что у кого-то Блэкджек.
     * Если да, то вычисляет результат игры и заканчивает игру.
     *
     * @return пустую строку если игра продолжается и непустую с сообщениями если есть Блэкджек
     */
    public String doIfAnyoneHasBlackjack() {
        StringBuilder sb = new StringBuilder();
        boolean hasPlayerBlackjack = roundState.isPlayerHasBlackjack();
        boolean hasDealerBlackjack = roundState.isDealerHasBlackjack();
        if (hasPlayerBlackjack && hasDealerBlackjack) {
            sb.append(roundState.dealerOpenSecondCard()).append("\n");
            sb.append(scoreState.drawPush("У вас и у дилера Блэкджек со старта! "));
        } else if (hasPlayerBlackjack)  {
            sb.append(roundState.dealerOpenSecondCard()).append("\n");
            sb.append(scoreState.playerWon("У вас Блэкджек со старта!"));
        } else if (hasDealerBlackjack) {
            sb.append(roundState.dealerOpenSecondCard()).append("\n");
            sb.append(scoreState.dealerWon("У дилера Блэкджек со старта!"));
        }
        return sb.toString();
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
     * @return пустую строку если игра продолжается и непустую если перебор
     */
    public String doIfPlayerIsBusted() {
        if (roundState.isPlayerBusted()) {
            return scoreState.dealerWon("У вас перебор!");
        }
        return "";
    }

    /**
     * Метод, который симулирует процесс взятия дилером карт до остановки.
     *
     * @return все текстовые сообщения в одной строке
     */
    public String processDealerTakes() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nХод дилера\n").append("-------\n");
        sb.append(roundState.dealerOpenSecondCard()).append("\n");
        sb.append(roundState.getStateInformation()).append("\n");

        while (roundState.isDealerMustToMove()) {
            sb.append(roundState.dealerTakeCard()).append("\n");
            sb.append(roundState.getStateInformation()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Метод, который определяет, кто выиграл и печатает соответствующее сообщение.
     *
     * @return строка-сообщение с результатом раунда
     */
    public String getRoundResult() {
        if (roundState.isDealerBusted()) {
            return scoreState.playerWon("У дилера перебор!");
        } else {
            int whoWon = roundState.calculateWhoWonByScore();
            if (whoWon > 0) {
                return scoreState.playerWon("Вы набрали больше очков!");
            } else if (whoWon == 0) {
                return scoreState.drawPush("");
            } else {
                return scoreState.dealerWon("Дилер набрал больше очков.");
            }
        }
    }

    /**
     * Метод, который печатает результат.
     *
     * @return строка с результатом
     */
    public String getScore() {
        return scoreState.getScoreNotification();
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
     *
     * @return возвращает строку прощания
     */
    public String goodbye() {
        return "Спасибо за игру! Мы будем рады видеть Вас ещё!";
    }
}