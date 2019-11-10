package com.podo.sadream.telegram.client;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

@EqualsAndHashCode
@Getter
public class TMessageVo {
    Integer telegramId;
    Integer messageId;
    String message;
    String image;
    ReplyKeyboard keyboard;
    SentCallback<Message> callback;

    public TMessageVo(Integer telegramId, Integer messageId) {
        this.telegramId = telegramId;
        this.messageId = messageId;
    }

    private TMessageVo(Integer telegramId, Integer messageId, String message, String image, ReplyKeyboard keyboard, SentCallback<Message> callback) {
        this.telegramId = telegramId;
        this.messageId = messageId;
        this.message = message;
        this.image = image;
        this.keyboard = keyboard;
        this.callback = callback;
    }

    public TMessageVo newValue(String message, String image, ReplyKeyboard keyboard, SentCallback<Message> callback) {
        return new TMessageVo(this.telegramId, this.messageId, message, image, keyboard, callback);
    }

    public TMessageVo newValue(String message, ReplyKeyboard keyboard, SentCallback<Message> callback) {
        return new TMessageVo(this.telegramId, this.messageId, message, null, keyboard, callback);
    }

}

