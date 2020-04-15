package com.podo.helloprice.crawl.agent.global.infra.mq;

import com.podo.helloprice.crawl.agent.global.infra.mq.message.LastPublishedProduct;
import com.podo.helloprice.crawl.agent.global.infra.mq.message.NotifyEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MQConfig {

    @Bean
    public Consumer<LastPublishedProduct> consumeLastPublishProduct(CrawlProductJobConsumer crawlProductJobConsumer) {
        return crawlProductJobConsumer;
    }

    @Bean
    public Supplier<Flux<NotifyEvent>> publishNotifyEvent(NotifyEventPublisher notifyEventPublisher){
        return notifyEventPublisher::processor;
    }

}
