package com.podo.helloprice.telegram.api;

import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.vo.MessageVo;
import com.podo.helloprice.telegram.app.core.TelegramMessageSender;
import com.podo.helloprice.telegram.app.vo.SendMessageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramApi {

    private final TelegramMessageSender telegramMessageSender;
    private final SendMessageCallbackFactory callbackFactory;

    public void send(String telegramId, String imageUrl, String response) {
        final MessageVo messageVo = new MessageVo(telegramId, null);
        telegramMessageSender.send(SendMessageVo.create(messageVo, response, imageUrl, null, callbackFactory.create(telegramId + "", null)));
    }
}
