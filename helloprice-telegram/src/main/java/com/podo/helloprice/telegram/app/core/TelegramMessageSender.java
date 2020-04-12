package com.podo.helloprice.telegram.app.core;

import com.podo.helloprice.telegram.app.vo.SendMessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramMessageSender {

    private TelegramBot telegramBot;

    public void setBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
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
            telegramBot.execute(sendPhoto);
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
            telegramBot.executeAsync(sendMessage, sendmessageVo.getCallback());
        } catch (TelegramApiException e) {
            log.error("{} >> 메시지를 전송 할 수 없습니다 '{}'", telegramId, e.getMessage());
            sendmessageVo.getCallback().onException(null, null);
        }

    }


}
