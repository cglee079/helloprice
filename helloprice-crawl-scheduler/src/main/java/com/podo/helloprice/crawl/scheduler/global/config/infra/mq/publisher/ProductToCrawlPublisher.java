package com.podo.helloprice.crawl.scheduler.global.config.infra.mq.publisher;

import com.podo.helloprice.crawl.scheduler.global.config.infra.mq.message.ProductToCrawlMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.EmitterProcessor;

@Slf4j
@Component
public class ProductToCrawlPublisher {

    private EmitterProcessor<ProductToCrawlMessage> processor = EmitterProcessor.create();

    public void publish(ProductToCrawlMessage productToCrawlMessage){
        log.debug("MQ :: PUBLISH :: payload : {}", productToCrawlMessage);
        processor.onNext(productToCrawlMessage);
    }

    public EmitterProcessor<ProductToCrawlMessage> processor() {
        return processor;
    }

}
