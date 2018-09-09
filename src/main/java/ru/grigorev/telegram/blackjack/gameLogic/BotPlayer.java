package ru.grigorev.telegram.blackjack.gameLogic;

public class BotPlayer extends Player {
    @Override
    public String getStringHand() {
        StringBuilder sb = new StringBuilder("Bot's cards are: ");
        for (Integer integer : getHand()) {
            sb.append(integer).append(" ");
        }
        sb.append("\nSum is: ").append(getSumOfHand());
        return sb.toString();
    }
}
