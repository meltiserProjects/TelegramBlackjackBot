package ru.grigorev.telegram.blackjack;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.meta.logging.BotLogger;
import org.telegram.telegrambots.meta.logging.BotsFileHandler;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

/**
 * @author Dmitriy Grigorev
 */
public class Main {
    private static final String LOGTAG = "MAIN";

    static {
        ApiContextInitializer.init();
    }

    private static LongPollingBot bot = new Bot();

    public static void main(String[] args) {
        BotLogger.setLevel(Level.ALL);
        BotLogger.registerLogger(new ConsoleHandler());
        try {
            BotLogger.registerLogger(new BotsFileHandler());
        } catch (IOException e) {
            BotLogger.severe(LOGTAG, e);
        }
        botInitialization();
    }

    private static void botInitialization() {
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(bot);
            System.out.println("Bot is running...");
        } catch (TelegramApiException e) {
            BotLogger.severe(LOGTAG, e);
        }
    }
}
