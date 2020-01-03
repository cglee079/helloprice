package com.podo.helloprice.telegram.client;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

@EqualsAndHashCode
@Getter
public class TMessageVo {
    private String telegramId;
    private Integer messageId;
    private String message;
    private String image;
    private ReplyKeyboard keyboard;
    private SentCallback<Message> callback;

    public TMessageVo(String telegramId, Integer messageId) {
        this.telegramId = telegramId;
        this.messageId = messageId;
    }

    private TMessageVo(String telegramId, Integer messageId, String message, String image, ReplyKeyboard keyboard, SentCallback<Message> callback) {
        this.telegramId = telegramId;
        this.messageId = messageId;
        this.message = message;
        this.image = image;
        this.keyboard = keyboard;
        this.callback = callback;
    }

    public TMessageVo newMessage(String message, String image, ReplyKeyboard keyboard, SentCallback<Message> callback) {
        return new TMessageVo(this.telegramId, this.messageId, message, image, keyboard, callback);
    }

    public TMessageVo newMessage(String message, ReplyKeyboard keyboard, SentCallback<Message> callback) {
        return new TMessageVo(this.telegramId, this.messageId, message, null, keyboard, callback);
    }

}

