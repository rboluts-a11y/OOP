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
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(System.in, java.nio.charset.StandardCharsets.UTF_8);

        Deck deck = new Deck();
        int playerWins = 0, dealerWins = 0, roundNumber = 0;
        System.out.println("Добро пожаловать в Блэкджек!");

        int isPlayerWantToPlay = 1;
        while (isPlayerWantToPlay == 1) {
            roundNumber++;
            System.out.println("Раунд " + roundNumber);

            deck.reset();
            deck.shuffle();

            Hand playerHand = new Hand();
            Hand dealerHand = new Hand();

            playerHand.addCard(deck.takeCard());
            dealerHand.addCard(deck.takeCard());
            playerHand.addCard(deck.takeCard());
            dealerHand.addCard(deck.takeCard());

            System.out.println("Дилер раздал карты");
            System.out.println("    Ваши карты: " + playerHand.toStringFormatted(false));
            System.out.println("    Карты дилера: " + dealerHand.toStringFormatted(true) + "\n");

            if (playerHand.isBlackjack()) {
                System.out.print("У вас Блэкджек со старта! Вы выиграли раунд! ");
                playerWins++;

                System.out.println(getScoreNotification(playerWins, dealerWins));

                System.out.println("\nВведите “1”, если хотите сыграть ещё, и “0”, чтобы остановиться... ");
                isPlayerWantToPlay = scanner.nextInt();
                System.out.println();

                continue;
            }

            System.out.println("Ваш ход");
            System.out.println("-------");

            while (!playerHand.isBusted()) {
                System.out.println("Введите “1”, чтобы взять карту, и “0”, чтобы остановиться... ");
                int choice = scanner.nextInt();
                if (choice == 0) {
                    break;
                }

                if (choice == 1) {
                    Card takenCard = deck.takeCard();
                    playerHand.addCard(takenCard);
                    System.out.println("Вы открыли карту " + takenCard.toStringWithPoints(false));
                    System.out.println("    Ваши карты: " + playerHand.toStringFormatted(false));
                    System.out.println("    Карты дилера: " + dealerHand.toStringFormatted(true) + "\n");
                }
            }

            if (playerHand.isBusted()) {
                System.out.print("У вас перебор! Вы проиграли раунд. ");
                dealerWins++;

                System.out.println(getScoreNotification(playerWins, dealerWins));

                System.out.println("\nВведите “1”, если хотите сыграть ещё, и “0”, чтобы остановиться... ");
                isPlayerWantToPlay = scanner.nextInt();
                System.out.println();

                continue;
            }

            System.out.println("\nХод дилера");
            System.out.println("-------");

            waitSeconds(1.0);

            Card openedCard = dealerHand.openSecondCard();
            System.out.println("Дилер открывает закрытую карту " + openedCard.toStringWithPoints(false));
            System.out.println("    Ваши карты: " + playerHand.toStringFormatted(false));
            System.out.println("    Карты дилера: " + dealerHand.toStringFormatted(false) + "\n");

            while (dealerHand.getScore() < 17) {
                waitSeconds(1.5);
                Card takenCard = deck.takeCard();
                dealerHand.addCard(takenCard);
                System.out.println("Дилер открывает карту " + takenCard.toStringWithPoints(false));
                System.out.println("    Ваши карты: " + playerHand.toStringFormatted(false));
                System.out.println("    Карты дилера: " + dealerHand.toStringFormatted(false) + "\n");
            }
            waitSeconds(1.0);

            int playerScore = playerHand.getScore();
            int dealerScore = dealerHand.getScore();

            if (dealerHand.isBusted()) {
                System.out.print("У дилера перебор! Вы выиграли раунд! ");
                playerWins++;
            } else if (playerScore > dealerScore) {
                System.out.print("Вы набрали больше очков! Вы выиграли раунд! ");
                playerWins++;
            } else if (dealerScore > playerScore) {
                System.out.print("Дилер набрал больше очков. Вы проиграли раунд. ");
                dealerWins++;
            } else {
                System.out.print("Ничья в раунде (Пуш)! Очки равны. ");
            }

            System.out.println(getScoreNotification(playerWins, dealerWins));

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
    private static void waitSeconds(double seconds) {
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Формирует текстовое сообщение о текущем счёте игры и лидере.
     * Возвращает строку в формате, удобном человеку, но ничего не печатает.
     *
     * @param playerWins количество побед игрока
     * @param dealerWins количество побед дилера
     * @return отформатированная строка со счётом
     */
    public static String getScoreNotification(int playerWins, int dealerWins) {
        if (playerWins > dealerWins) {
            return "Счет " + playerWins + ":" + dealerWins + " в вашу пользу.";
        } else if (dealerWins > playerWins) {
            return "Счет " + playerWins + ":" + dealerWins + " в пользу дилера.";
        } else {
            return "Счет " + playerWins + ":" + dealerWins + " (Ничья по раундам).";
        }
    }
}