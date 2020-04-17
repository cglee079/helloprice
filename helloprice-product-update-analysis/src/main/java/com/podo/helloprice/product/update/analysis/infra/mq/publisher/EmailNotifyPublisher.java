package com.podo.helloprice.product.update.analysis.infra.mq.publisher;

import com.podo.helloprice.product.update.analysis.infra.mq.message.EmailNotifyMessage;
import com.podo.helloprice.product.update.analysis.infra.mq.message.TelegramNotifyMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class EmailNotifyPublisher {

    private final EmitterProcessor<TelegramNotifyMessage> processor  = EmitterProcessor.create();

    public void publish(EmailNotifyMessage emailNotifyMessage) {
        log.debug("MQ :: PUBLISH :: payload : {}", emailNotifyMessage);
    }

    public Flux<TelegramNotifyMessage> processor() {
        return this.processor;
    }

}
