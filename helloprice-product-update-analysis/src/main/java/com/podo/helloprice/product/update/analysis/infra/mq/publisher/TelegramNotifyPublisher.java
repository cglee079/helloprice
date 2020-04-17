package com.podo.helloprice.product.update.analysis.infra.mq.publisher;

import com.podo.helloprice.product.update.analysis.infra.mq.message.TelegramNotifyMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class TelegramNotifyPublisher {

    private final EmitterProcessor<TelegramNotifyMessage> processor  = EmitterProcessor.create();

    public void publish(TelegramNotifyMessage telegramNotifyMessage){
        log.debug("MQ :: PUBLISH :: payload : {}", telegramNotifyMessage);

        processor.onNext(telegramNotifyMessage);
    }

    public Flux<TelegramNotifyMessage> processor() {
        return this.processor;
    }
}
