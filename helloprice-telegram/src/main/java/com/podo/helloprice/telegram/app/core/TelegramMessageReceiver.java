package com.podo.helloprice.telegram.app.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class TelegramMessageReceiver extends TelegramLongPollingBot {

    private final String botToken;
    private final String botUsername;
    private final TelegramMessageReceiverHandler telegramMessageReceiverHandler;

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (Objects.nonNull(update.getEditedMessage())) {
            telegramMessageReceiverHandler.receive(update.getEditedMessage());
            return;
        }

        telegramMessageReceiverHandler.receive(update.getMessage());
    }
}
