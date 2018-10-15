package ru.grigorev.telegram.blackjack;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;
import ru.grigorev.telegram.blackjack.gameLogic.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.toIntExact;

/**
 * @author Dmitriy Grigorev
 */
public class Bot extends TelegramLongPollingBot {
    private static final String LOGTAG = "BOT";
    private Long chatId;
    private Game game;
    private Map<Long, Game> userGameMap = new HashMap<>();

    public Bot(DefaultBotOptions options) {
        super(options);
    }

    public Bot() {
    }

    @Override
    public void onUpdateReceived(Update update) {
        BotLogger.info(LOGTAG, "Update recieved");
        // check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessagesFromClient(update);
        } else if (update.hasCallbackQuery()) {
            handleCallBackQueryFromClient(update);
        }
    }

    private void handleCallBackQueryFromClient(Update update) {
        setChatId(update.getCallbackQuery().getMessage().getChatId());
        checkAndInitGameForUser();
        String receivedCallbackQuery = update.getCallbackQuery().getData();
        try {
            if (receivedCallbackQuery.equals(Commands.NEW_GAME)) {
                sendMessage("Game is on!");
                game.init(this);
            }
            if (receivedCallbackQuery.equals(Commands.HIT)) {
                if (game.isRunning()) game.hit();
                else sendNewGameButton();
            }
            if (receivedCallbackQuery.equals(Commands.STAND)) {
                if (game.isRunning()) game.stand();
                else sendNewGameButton();
            }
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    private void handleMessagesFromClient(Update update) {
        setChatId(update.getMessage().getChatId());
        checkAndInitGameForUser();
        String receivedText = update.getMessage().getText();
        try {
            if (receivedText.equals(Commands.START)) {
                sendMessageWithButton("Hello and welcome to BlackJack!" +
                                " Type /newgame for a new game or simply push the button!",
                        "New game!",
                        Commands.NEW_GAME);
                if (game != null) game.clearStat();
            }
            if (receivedText.equals(Commands.NEW_GAME)) {
                sendMessage("Game is on!");
                game.init(this);
            }
            if (receivedText.equals(Commands.HIT)) {
                if (game.isRunning()) game.hit();
                else sendNewGameButton();
            }
            if (receivedText.equals(Commands.STAND)) {
                if (game.isRunning()) game.stand();
                else sendNewGameButton();
            }
            if (receivedText.equals(Commands.STAT)) {
                if (game != null) game.sendStat();
            }
            if (receivedText.equals(Commands.CLEAR_STAT)) {
                if (game != null) game.clearStat();
            }
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    private void checkAndInitGameForUser() {
        if (userGameMap.containsKey(chatId)) {
            this.game = userGameMap.get(chatId);
        } else {
            Game gameForPlayer = new Game();
            userGameMap.put(chatId, gameForPlayer);
            this.game = gameForPlayer;
        }
    }

    public void sendMessageWithButton(String message, String buttonName, String callback) throws TelegramApiException {
        SendMessage sMessageObjButton = new SendMessage() // Create a message object object
                .setChatId(chatId)
                .setText(message);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText(buttonName).setCallbackData(callback));
        rowsInline.add(rowInline); // Set the keyboard to the markup
        markupInline.setKeyboard(rowsInline); // Add it to the message
        sMessageObjButton.setReplyMarkup(markupInline);
        execute(sMessageObjButton); // Sending our message object to user
    }

    public void sendMessageWithTwoButtons(String message, String buttonName1, String buttonName2,
                                          String callBack1, String callback2) throws TelegramApiException {
        SendMessage sMessageObjButton = new SendMessage()
                .setChatId(chatId)
                .setText(message);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText(buttonName1).setCallbackData(callBack1));
        rowInline.add(new InlineKeyboardButton().setText(buttonName2).setCallbackData(callback2));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        sMessageObjButton.setReplyMarkup(markupInline);
        execute(sMessageObjButton);
    }

    public void sendMessage(String message) throws TelegramApiException {
        SendMessage sendMessageObj = new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(chatId)
                .setText(message);
        execute(sendMessageObj);
    }

    public void sendMessageInsteadButton(String message, long messageId) throws TelegramApiException {
        EditMessageText new_message = new EditMessageText()
                .setChatId(chatId)
                .setMessageId(toIntExact(messageId))
                .setText(message);
        execute(new_message);
    }

    public void sendNewGameButton() throws TelegramApiException {
        sendMessageWithButton("One more time?",
                "New Game!",
                Commands.NEW_GAME);
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    @Override
    public String getBotUsername() {
        return Security.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return Security.getBotToken();
    }
}