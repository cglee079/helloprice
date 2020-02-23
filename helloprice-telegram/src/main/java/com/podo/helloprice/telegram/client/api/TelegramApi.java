package com.podo.helloprice.telegram.client.api;

import com.podo.helloprice.telegram.client.TMessageCallbackFactory;
import com.podo.helloprice.telegram.client.vo.TMessageVo;
import com.podo.helloprice.telegram.client.core.TelegramMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramApi {

    private final TelegramMessageSender telegramMessageSender;
    private final TMessageCallbackFactory callbackFactory;

    public void send(String telegramId, String image, String response) {
        final TMessageVo tMessageVo = new TMessageVo(telegramId, null);

        telegramMessageSender.send(tMessageVo.newMessage(response, image, null, callbackFactory.create(telegramId + "", null)));
    }
}
