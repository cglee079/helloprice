package com.podo.helloprice.notify.analysis.infra.mq.consumer;

import com.podo.helloprice.core.model.ProductUpdateStatus;
import com.podo.helloprice.notify.analysis.infra.mq.message.NotifyEventMessage;
import com.podo.helloprice.notify.analysis.infra.mq.message.TelegramNotifyMessage;
import com.podo.helloprice.notify.analysis.infra.mq.publish.TelegramNotifyPublisher;
import com.podo.helloprice.notify.analysis.notify.vo.TelegramNotify;
import com.podo.helloprice.notify.analysis.notify.generator.TelegramNotifyGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotifyEventConsumer implements Consumer<NotifyEventMessage> {

    private final TelegramNotifyPublisher telegramNotifyPublisher;

    private final Map<ProductUpdateStatus, TelegramNotifyGenerator> telegramNotifyGenerators;


    @Override
    public void accept(NotifyEventMessage notifyEventMessage) {
        log.debug("MQ :: CONSUME :: payload : {}", notifyEventMessage);

        final Long productId = notifyEventMessage.getProductId();
        final ProductUpdateStatus updateStatus = notifyEventMessage.getUpdateStatus();

        notifyTelegram(productId, updateStatus);
    }

    private void notifyTelegram(Long productId, ProductUpdateStatus updateStatus) {
        final List<TelegramNotify> telegramNotifies = telegramNotifyGenerators.get(updateStatus).generate(productId);
        for (TelegramNotify telegramNotify : telegramNotifies) {
            telegramNotifyPublisher.publish(new TelegramNotifyMessage(telegramNotify));
        }
    }
}
