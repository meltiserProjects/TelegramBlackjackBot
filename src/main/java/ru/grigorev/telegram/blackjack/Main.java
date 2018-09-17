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
    private static LongPollingBot bot;

    public static void main(String[] args) {
        loggerInitialization();
        botInitialization();
    }

    private static void botInitialization() {
        ApiContextInitializer.init();
        bot = new Bot();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            BotLogger.info(LOGTAG, "Try initialization...");
            botsApi.registerBot(bot);
            BotLogger.info(LOGTAG, "Bot is running...");
        } catch (TelegramApiException e) {
            BotLogger.severe(LOGTAG, e);
        }
    }

    private static void loggerInitialization() {
        BotLogger.setLevel(Level.ALL);
        BotLogger.registerLogger(new ConsoleHandler());
        try {
            BotLogger.registerLogger(new BotsFileHandler());
        } catch (IOException e) {
            BotLogger.severe(LOGTAG, e);
        }
    }
}
