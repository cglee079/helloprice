package com.podo.helloprice.telegram.client.core;

import com.podo.helloprice.telegram.client.vo.TMessageVo;
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

    public void sendWithWebPagePreview(TMessageVo tMessageVo) {
        if (!StringUtils.isEmpty(tMessageVo.getImage())) {
            sendPhoto(tMessageVo);
        }

        sendMessage(tMessageVo, true);
    }

    public void send(TMessageVo tMessageVo) {
        if (!StringUtils.isEmpty(tMessageVo.getImage())) {
            sendPhoto(tMessageVo);
        }

        sendMessage(tMessageVo, false);
    }

    private void sendPhoto(TMessageVo tMessageVo) {
        final SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(tMessageVo.getTelegramId());
        sendPhoto.setPhoto(tMessageVo.getImage());
        sendPhoto.setReplyMarkup(tMessageVo.getKeyboard());
        sendPhoto.setReplyToMessageId(tMessageVo.getMessageId());
        sendPhoto.disableNotification();

        try {
            telegramBot.execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error("{} >> 이미지를 전송 할 수 없습니다. Image : {}", tMessageVo.getTelegramId(), tMessageVo.getImage());
        }
    }


    private void sendMessage(TMessageVo tMessageVo, Boolean enabledWebPreview) {
        final String telegramId = tMessageVo.getTelegramId();
        final SendMessage sendMessage = new SendMessage(tMessageVo.getTelegramId(), tMessageVo.getMessage());

        sendMessage.setReplyMarkup(tMessageVo.getKeyboard());
        sendMessage.setReplyToMessageId(tMessageVo.getMessageId());
        sendMessage.enableHtml(true);

        if (!enabledWebPreview) {
            sendMessage.disableWebPagePreview();
        }

        try {
            log.info("{} >> 메세지 전송, 보낸 메세지 : {}", telegramId, tMessageVo.getMessage().replace("\n", " "));
            telegramBot.executeAsync(sendMessage, tMessageVo.getCallback());
        } catch (TelegramApiException e) {
            log.error("{} >> 메시지를 전송 할 수 없습니다 '{}'", telegramId, e.getMessage());
            tMessageVo.getCallback().onException(null, null);
        }

    }


}
