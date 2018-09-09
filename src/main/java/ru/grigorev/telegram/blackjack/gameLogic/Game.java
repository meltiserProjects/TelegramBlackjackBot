package ru.grigorev.telegram.blackjack.gameLogic;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.grigorev.telegram.blackjack.Bot;
import ru.grigorev.telegram.blackjack.Commands;

public class Game {
    private Player player;
    private Player botPlayer;
    private Bot telegramBot;
    private boolean isRunning;
    private int botsWin;
    private int playersWin;

    public void init(Bot bot) throws TelegramApiException {
        telegramBot = bot;
        player = new Player();
        botPlayer = new BotPlayer();
        isRunning = true;

        printPlayersHand(player);
        printPlayersHand(botPlayer);
        telegramBot.sendMessageWithTwoButtons("/hit or /stand?",
                "Hit!", "Stand!",
                Commands.HIT, Commands.STAND);
    }

    public void hit() throws TelegramApiException {
        player.appendCardInHand();
        player.replaceAceCardIfBusted();
        printPlayersHand(player);
        if (player.isBustedHand()) {
            isRunning = false;
            printResults();
        }
    }

    public void stand() throws TelegramApiException {
        while (botPlayer.getSumOfHand() <= player.getSumOfHand() && botPlayer.getSumOfHand() != 21) {
            //a little delay
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            botPlayer.appendCardInHand();
            botPlayer.replaceAceCardIfBusted();
            printPlayersHand(botPlayer);
        }
        isRunning = false;
        printResults();
    }

    public void printResults() throws TelegramApiException {
        if (player.isBustedHand()) {
            telegramBot.sendMessage("\nBusted!\nComputer won!");
            botsWin++;
        } else if (botPlayer.isBustedHand()) {
            telegramBot.sendMessage("Bot is a buster!\nYou won!");
            playersWin++;
        } else if (player.getSumOfHand() < botPlayer.getSumOfHand()) {
            telegramBot.sendMessage("You didn't get enough...\nBot won!");
            botsWin++;
        } else if (player.getSumOfHand() == botPlayer.getSumOfHand())
            telegramBot.sendMessage("Sums are equal\nIt's a draw!");
        telegramBot.sendNewGameButton();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void sendStat() throws TelegramApiException {
        telegramBot.sendMessage("Players win: " + playersWin + ", Bots win: " + botsWin);
    }

    public void printPlayersHand(Player player) throws TelegramApiException {
        telegramBot.sendMessage(player.getStringHand());
    }
}
