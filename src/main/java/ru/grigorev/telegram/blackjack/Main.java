package ru.grigorev.telegram.blackjack;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
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
        botInitialization(true);
    }

    private static void botInitialization(boolean isProxy) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        if (isProxy) {
            bot = new Bot(proxyInitialization());
        } else {
            bot = new Bot();
        }
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
        try {
            BotLogger.registerLogger(new BotsFileHandler());
        } catch (IOException e) {
            BotLogger.severe(LOGTAG, e);
        }
    }

    private static DefaultBotOptions proxyInitialization() {
        // to change proxy address see "Security" class
        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

        HttpHost httpHost = new HttpHost(Security.getProxyHost(), Security.getProxyPort());

        RequestConfig requestConfig = RequestConfig.custom().setProxy(httpHost).setAuthenticationEnabled(false).build();
        botOptions.setRequestConfig(requestConfig);
        botOptions.setProxyHost(Security.getProxyHost());
        botOptions.setProxyPort(Security.getProxyPort());
        botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
        return botOptions;
    }
}
