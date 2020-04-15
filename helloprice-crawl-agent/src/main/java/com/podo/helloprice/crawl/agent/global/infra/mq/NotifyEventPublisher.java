package com.podo.helloprice.crawl.agent.global.infra.mq;

import com.podo.helloprice.crawl.agent.global.infra.mq.message.NotifyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class NotifyEventPublisher{

    public final EmitterProcessor<NotifyEvent> processor  = EmitterProcessor.create();

    public void publish(NotifyEvent notifyEvent){
        processor.onNext(notifyEvent);
    }

    public Flux<NotifyEvent> processor() {
        return this.processor;
    }
}
