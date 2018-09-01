package ru.grigorev.telegram.blackjack;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.grigorev.telegram.blackjack.gameLogic.Game;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

/**
 * @author Dmitriy Grigorev
 */
public class Bot extends TelegramLongPollingBot {
    private Long chatId;

    @Override
    public void onUpdateReceived(Update update) {
        // check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            setChatId(update.getMessage().getChatId());
            try {
                if (update.getMessage().getText().equals(Commands.START)) {
                    Game.init(this);
                }
                if (update.getMessage().getText().equals(Commands.HIT)) {
                    if (Game.isRunning()) Game.hit();
                    else sendMessageWithButton("Type /start to start the game!",
                            "Start!",
                            Commands.START);
                }
                if (update.getMessage().getText().equals(Commands.STAND)) {
                    if (Game.isRunning()) Game.stand();
                    else sendMessageWithButton("Type /start to start the game!",
                            "Start!",
                            Commands.START);
                }
                if (update.getMessage().getText().equals(Commands.STAT)) {
                    Game.sendStat();
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery()) {
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            try {
                if (update.getCallbackQuery().getData().equals(Commands.START)) {
                    sendMessageInsteadButton("Let's get started!", message_id);
                    Game.init(this);
                }
                if (update.getCallbackQuery().getData().equals(Commands.HIT)) {
                    if (Game.isRunning()) Game.hit();
                    else sendMessageWithButton("Type /start to start the game!",
                            "Start!",
                            Commands.START);
                }
                if (update.getCallbackQuery().getData().equals(Commands.STAND)) {
                    if (Game.isRunning()) Game.stand();
                    else sendMessageWithButton("Type /start to start the game!",
                            "Start!",
                            Commands.START);
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
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
        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        sMessageObjButton.setReplyMarkup(markupInline);
        execute(sMessageObjButton); // Sending our message object to user
    }

    public void sendMessageWithTwoButtons(String message, String buttonName1, String buttonName2,
                                          String callBack1, String callback2) throws TelegramApiException {
        SendMessage sMessageObjButton = new SendMessage() // Create a message object object
                .setChatId(chatId)
                .setText(message);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText(buttonName1).setCallbackData(callBack1));
        rowInline.add(new InlineKeyboardButton().setText(buttonName2).setCallbackData(callback2));
        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        sMessageObjButton.setReplyMarkup(markupInline);
        execute(sMessageObjButton); // Sending our message object to user
    }

    public void sendMessage(String message) throws TelegramApiException {
        SendMessage sendMessageObj = new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(chatId)
                .setText(message); //update.getMessage().getText()
        execute(sendMessageObj);
    }

    public void sendMessageInsteadButton(String message, long message_id) {
        EditMessageText new_message = new EditMessageText()
                .setChatId(chatId)
                .setMessageId(toIntExact(message_id))
                .setText(message);
        try {
            execute(new_message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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