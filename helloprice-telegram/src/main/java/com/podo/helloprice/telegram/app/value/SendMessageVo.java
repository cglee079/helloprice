package com.podo.helloprice.telegram.app.value;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

@EqualsAndHashCode
@Getter
public class SendMessageVo {

    private String telegramId;
    private Integer messageId;
    private String message;
    private String image;
    private ReplyKeyboard keyboard;
    private SentCallback<Message> callback;

    private SendMessageVo(MessageVo messageVo, String message, String image, ReplyKeyboard keyboard, SentCallback<Message> callback) {
        this.telegramId = messageVo.getTelegramId();
        this.messageId = messageVo.getMessageId();
        this.message = message;
        this.image = image;
        this.keyboard = keyboard;
        this.callback = callback;
    }

    public static SendMessageVo create(MessageVo messageVo, String message, String image, ReplyKeyboard keyboard, SentCallback<Message> callback) {
        return new SendMessageVo(messageVo, message, image, keyboard, callback);
    }

    public static SendMessageVo create(MessageVo messageVo, String message, ReplyKeyboard keyboard, SentCallback<Message> callback) {
        return new SendMessageVo(messageVo, message, null, keyboard, callback);
    }

}

