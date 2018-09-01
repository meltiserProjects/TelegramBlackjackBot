package ru.grigorev.telegram.blackjack.gameLogic;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.grigorev.telegram.blackjack.Bot;
import ru.grigorev.telegram.blackjack.Commands;

public class Game {
    private static Player player;
    private static BotPlayer botPlayer;
    private static Bot telegramBot;
    private static boolean isRunning;
    private static int botsWin;
    private static int playersWin;

    public static void init(Bot bot) throws TelegramApiException {
        telegramBot = bot;
        player = new Player(bot);
        botPlayer = new BotPlayer(bot);
        isRunning = true;

        telegramBot.sendMessage("Welcome to blackjack!");
        player.printHand();
        botPlayer.printHand();
        telegramBot.sendMessageWithTwoButtons("/hit or /stand?",
                "Hit!", "Stand!",
                Commands.HIT, Commands.STAND);
    }

    public static void hit() throws TelegramApiException {
        player.appendCardInHand();
        player.replaceAceCardIfBusted();
        player.printHand();
        if (player.isBustedHand()) {
            isRunning = false;
            printResults();
        }
    }

    public static void stand() throws TelegramApiException {
        while (botPlayer.getSumOfHand() <= player.getSumOfHand() && botPlayer.getSumOfHand() != 21) {
            //a little delay
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            botPlayer.appendCardInHand();
            botPlayer.replaceAceCardIfBusted();
            botPlayer.printHand();
        }
        isRunning = false;
        printResults();
    }

    public static void printResults() throws TelegramApiException {
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
        telegramBot.sendMessageWithButton("Type /start to start the game!",
                "Start!",
                Commands.START);
    }

    public static boolean isRunning() {
        return isRunning;
    }

    public static void sendStat() throws TelegramApiException {
        telegramBot.sendMessage("Players win: " + playersWin + ", Bots win: " + botsWin);
    }
}
