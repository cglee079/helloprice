package com.podo.helloprice.notify.analysis.infra.mq;

import com.podo.helloprice.notify.analysis.infra.mq.consumer.NotifyEventConsumer;
import com.podo.helloprice.notify.analysis.infra.mq.message.NotifyEventMessage;
import com.podo.helloprice.notify.analysis.infra.mq.message.TelegramNotifyMessage;
import com.podo.helloprice.notify.analysis.infra.mq.publish.TelegramNotifyPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MQConfig {

    @Bean
    public Consumer<NotifyEventMessage> consumeNotifyEvent(NotifyEventConsumer notifyEventConsumer) {
        return notifyEventConsumer;
    }

    @Bean
    public Supplier<Flux<TelegramNotifyMessage>> publishTelegramNotify(TelegramNotifyPublisher telegramNotifyPublisher){
        return telegramNotifyPublisher::processor;
    }
}
