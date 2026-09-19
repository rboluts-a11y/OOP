package org.example;

/**
 * Класс, в котором хранится состояние всей игры по раундам: количество игр, побед, поражений
 */
public class ScoreState {
    private int playerWins = 0;
    private int dealerWins = 0;
    private int roundNumber = 0;

    /**
     * Добавляет новую игру.
     */
    public String addNewGame() {
        roundNumber++;
        return "Раунд " + roundNumber;
    }

    /**
     * Игрок победил
     *
     * @param reason информация о том, по какому правилу выиграл игрок
     *
     * @return строка с сообщением о победе
     */
    public String playerWon(String reason) {
        playerWins++;
        return reason + " Вы выиграли раунд! ";
    }

    /**
     * Дилер победил
     *
     * @param reason информация о том, по какому правилу выиграл дилер
     *
     * @return строка с сообщением о проигрыше
     */
    public String dealerWon(String reason) {
        dealerWins++;
        return reason + " Вы проиграли раунд. ";
    }

    /**
     * @return строка с сообщением о ничьей
     */
    public String drawPush() {
        return "Ничья в раунде (Пуш)! Очки равны. ";
    }

    /**
     * Формирует текстовое сообщение о текущем счёте игры и лидере.
     * Возвращает строку в формате, удобном человеку, но ничего не печатает.
     *
     * @return отформатированная строка со счётом
     */
    public String getScoreNotification() {
        if (playerWins > dealerWins) {
            return "Счет " + playerWins + ":" + dealerWins + " в вашу пользу.";
        } else if (dealerWins > playerWins) {
            return "Счет " + playerWins + ":" + dealerWins + " в пользу дилера.";
        } else {
            return "Счет " + playerWins + ":" + dealerWins + " (Ничья по раундам).";
        }
    }
}