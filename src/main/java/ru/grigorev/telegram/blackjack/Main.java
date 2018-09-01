package ru.grigorev.telegram.blackjack;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

/**
 * @author Dmitriy Grigorev
 */
public class Main {
    static {
        ApiContextInitializer.init();
    }

    private static LongPollingBot bot = new Bot();

    public static void main(String[] args) {
        botInitialization();
    }

    private static void botInitialization() {
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(bot);
            System.out.println("Bot is running...");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
