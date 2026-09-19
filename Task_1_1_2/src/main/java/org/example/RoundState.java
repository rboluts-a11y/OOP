package org.example;

/**
 *Класс, который хранит всю необходимую информацию о текущем раунде.
 *Реализованы необходимые методы для работы с
 */
public class RoundState {
    private Deck deck;
    private Hand playerHand;
    private Hand dealerHand;
    private boolean hideSecondCard;

    /**
     *Конструктор класса
     */
    public RoundState() {
        deck = new Deck();
        playerHand = new Hand();
        dealerHand = new Hand();
        hideSecondCard = true;
    }

    /**
     *
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
     *
     */
    public String getStateInformation() {
        return  "    Ваши карты: " + playerHand.toStringFormatted(false) + "\n" +
                "    Карты дилера: " + dealerHand.toStringFormatted(hideSecondCard) + "\n";
    }

    /**
     *
     */
    public boolean isPlayerWonByBlackjack() {
        return playerHand.isBlackjack() && hideSecondCard;
    }

    /**
     *
     */
    public boolean isPlayerBusted() {
        return playerHand.isBusted();
    }

    /**
     *
     * @return
     */
    public boolean isDealerBusted() {
        return dealerHand.isBusted();
    }

    public String playerTakeCard() {
        Card takenCard = deck.takeCard();
        playerHand.addCard(takenCard);
        return "Вы открыли карту " + takenCard.toStringWithPoints(false);
    }

    public String dealerTakeCard() {
        Card takenCard = deck.takeCard();
        dealerHand.addCard(takenCard);
        return "Дилер открывает карту " + takenCard.toStringWithPoints(false);
    }

    public String dealerOpenSecondCard() {
        hideSecondCard = false;
        Card secondDealerCard = dealerHand.openSecondCard();
        return "Дилер открывает закрытую карту " + secondDealerCard.toStringWithPoints(false);
    }

    public boolean isDealerMustToMove() {
        return dealerHand.getScore() < 17;
    }

    public int calculateWhoWonByScore() {
        int playerScore = playerHand.getScore();
        int dealerScore = dealerHand.getScore();

        return Integer.compare(playerScore, dealerScore);
    }
}