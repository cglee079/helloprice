package com.podo.helloprice.notify.doer.mq;

import com.podo.helloprice.notify.doer.telegram.TelegramSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
@Component
public class TelegramNotifyMessageConsumer implements Consumer<TelegramNotifyMessage> {

    private final TelegramSender telegramSender;

    @Override
    public void accept(TelegramNotifyMessage telegramNotifyMessage) {
        log.debug("MQ :: CONSUME :: {}", telegramNotifyMessage);

        final String telegramId = telegramNotifyMessage.getTelegramId();
        final String contents = telegramNotifyMessage.getContents();

        telegramSender.send(telegramId, null, contents);
    }
}
