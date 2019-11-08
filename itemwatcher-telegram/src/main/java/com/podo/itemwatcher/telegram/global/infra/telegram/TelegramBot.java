package com.podo.itemwatcher.telegram.global.infra.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramReceiveHandler telegramReceiveHandler;

    @PostConstruct
    public void init() {
        telegramReceiveHandler.setTelegramBot(this);
    }

    @Value("${telegram.podo_itemwatcher.bot.token}")
    private String botToken;

    @Value("${telegram.podo_itemwatcher.bot.name}")
    private String botUsername;

    @Value("${telegram.podo_itemwatcher.admin.id}")
    private String adminId;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        telegramReceiveHandler.receiveMessage(update);
    }

    public void send(Integer telegramId, Integer messageId, String message, ReplyKeyboard keyboard) {

        final SendMessage sendMessage = new SendMessage(telegramId.toString(), message);
        sendMessage.setReplyMarkup(keyboard);
        sendMessage.setReplyToMessageId(messageId);
        sendMessage.enableHtml(true);

        try {
            this.executeAsync(sendMessage, new SentCallback<Message>() {
                @Override
                public void onResult(BotApiMethod<Message> method, Message response) {
                    //No Logic
                }

                @Override
                public void onError(BotApiMethod<Message> method, TelegramApiRequestException e) {
                    log.error("메시지를 전송 할 수 없습니다 '{}'", e.getMessage());
                }

                @Override
                public void onException(BotApiMethod<Message> method, Exception e) {
                    log.error("메시지를 전송 할 수 없습니다 '{}'", e.getMessage());
                }
            });

        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }

    }
}
