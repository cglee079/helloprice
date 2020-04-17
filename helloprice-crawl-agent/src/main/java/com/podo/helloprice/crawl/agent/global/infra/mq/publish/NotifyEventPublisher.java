package com.podo.helloprice.crawl.agent.global.infra.mq.publish;

import com.podo.helloprice.crawl.agent.global.infra.mq.message.NotifyEventMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class NotifyEventPublisher{

    public final EmitterProcessor<NotifyEventMessage> processor  = EmitterProcessor.create();

    public void publish(NotifyEventMessage notifyEventMessage){
        log.debug("MQ :: PUBLISH :: payload : {}", notifyEventMessage);

        processor.onNext(notifyEventMessage);
    }

    public Flux<NotifyEventMessage> processor() {
        return this.processor;
    }
}
