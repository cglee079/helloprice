package com.podo.helloprice.crawl.agent.global.infra.mq.publish;

import com.podo.helloprice.core.model.ProductUpdateStatus;
import com.podo.helloprice.crawl.agent.global.infra.mq.message.ProductUpdateMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class ProductUpdatePublisher {

    public final EmitterProcessor<ProductUpdateMessage> processor  = EmitterProcessor.create();

    public void publish(ProductUpdateMessage productUpdateMessage){
        log.debug("MQ :: PUBLISH :: payload : {}", productUpdateMessage);

        processor.onNext(productUpdateMessage);
    }

    public Flux<ProductUpdateMessage> processor() {
        return this.processor;
    }

    @PostConstruct
    public void dd(){
        this.publish(new ProductUpdateMessage(60L, ProductUpdateStatus.UPDATE_EMPTY_AMOUNT));
    }
}
