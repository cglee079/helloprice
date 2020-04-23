package com.podo.helloprice.telegram.app.core;

import com.podo.helloprice.telegram.app.value.SendMessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class TelegramMessageSender extends DefaultAbsSender {

    private final String botToken;

    public TelegramMessageSender(String botToken) {
        super(ApiContext.getInstance(DefaultBotOptions.class));
        this.botToken = botToken;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void sendWithWebPagePreview(SendMessageVo sendmessageVo) {
        if (!StringUtils.isEmpty(sendmessageVo.getImage())) {
            sendPhoto(sendmessageVo);
        }

        sendMessage(sendmessageVo, true);
    }

    public void send(SendMessageVo sendmessageVo) {
        if (!StringUtils.isEmpty(sendmessageVo.getImage())) {
            sendPhoto(sendmessageVo);
        }

        sendMessage(sendmessageVo, false);
    }

    private void sendPhoto(SendMessageVo sendMessage) {
        final SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(sendMessage.getTelegramId());
        sendPhoto.setPhoto(sendMessage.getImage());
        sendPhoto.setReplyMarkup(sendMessage.getKeyboard());
        sendPhoto.setReplyToMessageId(sendMessage.getMessageId());
        sendPhoto.disableNotification();

        try {
            this.execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error("TELEGRAM :: {} >> 이미지를 전송 할 수 없습니다. Image : {}", sendMessage.getTelegramId(), sendMessage.getImage());
        }
    }


    private void sendMessage(SendMessageVo sendmessageVo, Boolean enabledWebPreview) {
        final String telegramId = sendmessageVo.getTelegramId();
        final SendMessage sendMessage = new SendMessage(sendmessageVo.getTelegramId(), sendmessageVo.getMessage());

        sendMessage.setReplyMarkup(sendmessageVo.getKeyboard());
        sendMessage.setReplyToMessageId(sendmessageVo.getMessageId());
        sendMessage.enableHtml(true);

        if (!enabledWebPreview) {
            sendMessage.disableWebPagePreview();
        }

        try {
            log.debug("TELEGRAM :: {} >> 메세지 전송, 보낸 메세지 : {}", telegramId, sendmessageVo.getMessage().replace("\n", " "));
            this.executeAsync(sendMessage, sendmessageVo.getCallback());
        } catch (TelegramApiException e) {
            log.error("{} >> 메시지를 전송 할 수 없습니다 '{}'", telegramId, e.getMessage());
            sendmessageVo.getCallback().onException(null, null);
        }

    }


}
