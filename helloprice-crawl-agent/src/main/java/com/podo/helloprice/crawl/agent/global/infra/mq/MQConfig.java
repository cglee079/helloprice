package com.podo.helloprice.crawl.agent.global.infra.mq;

import com.podo.helloprice.crawl.agent.global.infra.mq.consumer.CrawlProductJobConsumer;
import com.podo.helloprice.crawl.agent.global.infra.mq.message.CrawlProductMessage;
import com.podo.helloprice.crawl.agent.global.infra.mq.message.NotifyEventMessage;
import com.podo.helloprice.crawl.agent.global.infra.mq.publish.NotifyEventPublisher;
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
    public Consumer<CrawlProductMessage> consumeCrawlProduct(CrawlProductJobConsumer crawlProductJobConsumer) {
        return crawlProductJobConsumer;
    }

    @Bean
    public Supplier<Flux<NotifyEventMessage>> publishNotifyEvent(NotifyEventPublisher notifyEventPublisher){
        return notifyEventPublisher::processor;
    }

}
