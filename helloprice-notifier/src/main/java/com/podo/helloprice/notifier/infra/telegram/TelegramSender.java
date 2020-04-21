package com.podo.helloprice.notifier.infra.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramSender extends DefaultAbsSender {

    @Value("${infra.telegram.bot.token}")
    private String botToken;

    public TelegramSender() {
        super(ApiContext.getInstance(DefaultBotOptions.class));
    }

    public String getBotToken() {
        return botToken;
    }

    public void send(String telegramId, String imageUrl, String contents) {
        if (!StringUtils.isEmpty(imageUrl)) {
            sendPhoto(telegramId, imageUrl);
        }

        sendMessage(telegramId, contents);
    }

    private void sendPhoto(String telegramId, String imageUrl) {
        final SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(telegramId);
        sendPhoto.setPhoto(imageUrl);
        sendPhoto.disableNotification();

        try {
            this.execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error("TELEGRAM :: {} >> 이미지를 전송 할 수 없습니다. Image : {}", telegramId, imageUrl);
        }
    }


    private void sendMessage(String telegramId, String contents) {
        final SendMessage sendMessage = new SendMessage(telegramId, contents);

        sendMessage.disableWebPagePreview();
        sendMessage.enableHtml(true);

        try {
            log.debug("TELEGRAM :: {} >> 메세지 전송, 보낸 메세지 : {}", telegramId, contents.replace("\n", " "));
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("{} >> 메시지를 전송 할 수 없습니다 '{}'", telegramId, e.getMessage());
        }

    }
}
