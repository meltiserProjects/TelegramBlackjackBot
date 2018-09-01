package ru.grigorev.telegram.blackjack.gameLogic;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.grigorev.telegram.blackjack.Bot;

public class BotPlayer extends Player {
    public BotPlayer(Bot bot) {
        super(bot);
    }

    @Override
    public void printHand() throws TelegramApiException {
        StringBuilder sb = new StringBuilder("Bot's cards are: ");
        for (Integer integer : getHand()) {
            sb.append(integer).append(" ");
        }
        sb.append("\nSum is: ").append(getSumOfHand());
        getTelegramBot().sendMessage(sb.toString());
    }
}
