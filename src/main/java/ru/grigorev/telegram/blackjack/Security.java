package ru.grigorev.telegram.blackjack;

/**
 * @author Dmitriy Grigorev
 */
public final class Security {
    private static final String BOT_USERNAME = "";
    private static final String BOT_TOKEN = "";
    private static final String PROXY_HOST = "80.120.86.242";
    private static final Integer PROXY_PORT = 46771;

    private Security() {
    }

    public static String getBotUsername() {
        return BOT_USERNAME;
    }

    public static String getBotToken() {
        return BOT_TOKEN;
    }

    public static String getProxyHost() {
        return PROXY_HOST;
    }

    public static Integer getProxyPort() {
        return PROXY_PORT;
    }
}
