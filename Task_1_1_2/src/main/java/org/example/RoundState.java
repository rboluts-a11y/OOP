package org.example;

/**
 * Класс, который хранит всю необходимую информацию о текущем раунде.
 * Реализованы необходимые методы для работы с состоянием раунда.
 */
public class RoundState {
    private Deck deck;
    private Hand playerHand;
    private Hand dealerHand;
    private boolean hideSecondCard;

    /**
     * Конструктор класса.
     */
    public RoundState() {
        deck = new Deck();
        playerHand = new Hand();
        dealerHand = new Hand();
        hideSecondCard = true;
    }

    /**
     * Метод для создания нового раунда.
     */
    public void newRound() {
        deck.reset();
        deck.shuffle();
        playerHand.clear();
        dealerHand.clear();
        hideSecondCard = true;
        playerHand.addCard(deck.takeCard());
        dealerHand.addCard(deck.takeCard());
        playerHand.addCard(deck.takeCard());
        dealerHand.addCard(deck.takeCard());
    }

    /**
     * Возвращает информацию о состоянии текущего раунда, но не печатает.
     *
     * @return красиво отформатированную строку с информацией о руках игрока и дилера
     */
    public String getStateInformation() {
        return  "    Ваши карты: " + playerHand.toStringFormatted(false) + "\n"
                + "    Карты дилера: " + dealerHand.toStringFormatted(hideSecondCard) + "\n";
    }

    /**
     * Метод, определяющий, выиграл ли игрок сразу после раздачи карт (Блэкджек).
     *
     * @return соответствующее булевое значение
     */
    public boolean isPlayerWonByBlackjack() {
        return playerHand.isBlackjack() && hideSecondCard;
    }

    /**
     * Метод, определяющий, проиграл ли игрок из-за перебора очков.
     *
     * @return соответствующее булевое значение
     */
    public boolean isPlayerBusted() {
        return playerHand.isBusted();
    }

    /**
     * Метод, определяющий, проиграл ли дилер из-за перебора очков.
     *
     * @return соответствующее булевое значение
     */
    public boolean isDealerBusted() {
        return dealerHand.isBusted();
    }

    /**
     * Метод, который симулирует взятие карты игроком.
     *
     * @return строка-сообщение о взятой карте без печати
     */
    public String playerTakeCard() {
        Card takenCard = deck.takeCard();
        playerHand.addCard(takenCard);
        return "Вы открыли карту " + takenCard.toStringWithPoints(false);
    }

    /**
     * Метод, который симулирует взятие карты дилером.
     *
     * @return строка-сообщение о взятой карте без печати
     */
    public String dealerTakeCard() {
        Card takenCard = deck.takeCard();
        dealerHand.addCard(takenCard);
        return "Дилер открывает карту " + takenCard.toStringWithPoints(false);
    }

    /**
     * Метод, который открывает вторую карту дилера.
     *
     * @return строка-сообщение об открытой карте без печати
     */
    public String dealerOpenSecondCard() {
        hideSecondCard = false;
        Card secondDealerCard = dealerHand.openSecondCard();
        return "Дилер открывает закрытую карту " + secondDealerCard.toStringWithPoints(false);
    }

    /**
     * Метод, который определяет, должен ли дилер продолжать брать карты.
     *
     * @return соответствующее булевое значение
     */
    public boolean isDealerMustToMove() {
        return dealerHand.getScore() < 17;
    }

    /**
     * Метод, определяющий победителя, если дело дошло до подсчёта очков.
     *
     * @return целое число: больще 0, если игрок выиграл, 0, если ничья и меньше 0 если проиграл
     */
    public int calculateWhoWonByScore() {
        int playerScore = playerHand.getScore();
        int dealerScore = dealerHand.getScore();

        return Integer.compare(playerScore, dealerScore);
    }
}