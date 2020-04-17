package com.podo.helloprice.crawl.scheduler.global.config.infra.mq;

import com.podo.helloprice.crawl.scheduler.global.config.infra.mq.message.CrawlProductMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Configuration
public class MQConfig {

    @Bean
    public Supplier<Flux<CrawlProductMessage>> publishCrawlProduct(CrawlProductPublisher crawlProductPublisher) {
        return crawlProductPublisher::processor;
    }
}
