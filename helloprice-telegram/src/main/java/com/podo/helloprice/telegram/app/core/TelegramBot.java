package com.podo.helloprice.telegram.app.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramMessageReceiver telegramMessageReceiver;
    private final TelegramMessageSender telegramMessageSender;

    @Value("${infra.telegram.bot.token}")
    private String botToken;

    @Value("${infra.telegram.bot.name}")
    private String botUsername;

    public TelegramBot(TelegramMessageReceiver telegramMessageReceiver, TelegramMessageSender telegramMessageSender) {
        super(getMyBotOptions());
        this.telegramMessageReceiver = telegramMessageReceiver;
        this.telegramMessageSender = telegramMessageSender;
    }

    private static DefaultBotOptions getMyBotOptions() {
        final DefaultBotOptions myBotOptions = new DefaultBotOptions();
        myBotOptions.setMaxThreads(5);
        return myBotOptions;
    }

    @PostConstruct
    public void init() {
        telegramMessageSender.setBot(this);
    }

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

        if (Objects.nonNull(update.getEditedMessage())) {
            telegramMessageReceiver.receive(update.getEditedMessage());
            return;
        }

        telegramMessageReceiver.receive(update.getMessage());
    }
}