package com.podo.helloprice.crawl.scheduler.global.config.infra.mq;

import com.podo.helloprice.crawl.scheduler.global.config.infra.mq.message.CrawlProductMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.EmitterProcessor;

@Slf4j
@Component
public class CrawlProductPublisher {

    private EmitterProcessor<CrawlProductMessage> processor = EmitterProcessor.create();

    public void publish(CrawlProductMessage crawlProductMessage){
        log.debug("MQ :: PUBLISH :: payload : {}", crawlProductMessage);
        processor.onNext(crawlProductMessage);
    }

    public EmitterProcessor<CrawlProductMessage> processor() {
        return processor;
    }

}
