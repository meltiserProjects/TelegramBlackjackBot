package ru.grigorev.telegram.blackjack;

/**
 * @author Dmitriy Grigorev
 */
public final class Security {
    private static final String BOT_USERNAME = "Simple_Blackjack_bot";
    private static final String BOT_TOKEN = "587322702:AAFRYHaD_RjdyWw9UrB8i-pWqIlm7RLy25U";
    private static final String PROXY_HOST = "82.114.94.130";
    private static final Integer PROXY_PORT = 58962;

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
