package com.podo.helloprice.telegram.client;

import com.podo.helloprice.telegram.client.menu.MenuHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {


    private final TelegramMessageReceiver telegramMessageReceiver;
    private final TelegramMessageSender telegramMessageSender;
    private final List<MenuHandler> menuHandlers;

    @PostConstruct
    public void init() {
        telegramMessageSender.setBot(this);

        for (MenuHandler menuHandler : menuHandlers) {
            menuHandler.setSender(telegramMessageSender);
        }

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
        Message message = update.getMessage();

        if (Objects.nonNull(update.getEditedMessage())) {
            message = update.getEditedMessage();
        }

        telegramMessageReceiver.receive(message);
    }


}
