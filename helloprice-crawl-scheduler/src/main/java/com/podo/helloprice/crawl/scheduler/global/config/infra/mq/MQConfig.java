package com.podo.helloprice.crawl.scheduler.global.config.infra.mq;

import com.podo.helloprice.crawl.scheduler.global.config.infra.mq.message.ProductToCrawlMessage;
import com.podo.helloprice.crawl.scheduler.global.config.infra.mq.publisher.ProductToCrawlPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Configuration
public class MQConfig {

    @Bean
    public Supplier<Flux<ProductToCrawlMessage>> publishProductToCrawl(ProductToCrawlPublisher productToCrawlPublisher) {
        return productToCrawlPublisher::processor;
    }
}
