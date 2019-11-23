package com.podo.helloprice.telegram.client;

import com.podo.helloprice.telegram.client.menu.MenuHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

import static java.util.stream.Collectors.toMap;

@Slf4j

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramMessageReceiver telegramMessageReceiver;
    private final TelegramMessageSender telegramMessageSender;

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

    @Value("${telegram.podo_helloprice.bot.token}")
    private String botToken;

    @Value("${telegram.podo_helloprice.bot.name}")
    private String botUsername;

    @Value("${telegram.podo_helloprice.admin.id}")
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

        if (Objects.nonNull(update.getEditedMessage())) {
            telegramMessageReceiver.receive(update.getEditedMessage());
            return;
        }

        telegramMessageReceiver.receive(update.getMessage());
    }


}
