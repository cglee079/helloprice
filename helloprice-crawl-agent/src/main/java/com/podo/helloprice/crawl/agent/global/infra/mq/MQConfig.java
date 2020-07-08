package com.podo.helloprice.crawl.agent.global.infra.mq;

import com.podo.helloprice.crawl.agent.global.infra.mq.consumer.CrawlProductJobConsumer;
import com.podo.helloprice.crawl.agent.global.infra.mq.message.ProductToCrawlMessage;
import com.podo.helloprice.crawl.agent.global.infra.mq.message.ProductUpdateMessage;
import com.podo.helloprice.crawl.agent.global.infra.mq.publish.ProductUpdatePublisher;
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
    public Consumer<ProductToCrawlMessage> consumeProductToCrawl(CrawlProductJobConsumer crawlProductJobConsumer) {
        return crawlProductJobConsumer;
    }

    @Bean
    public Supplier<Flux<ProductUpdateMessage>> publishProductUpdate(ProductUpdatePublisher productUpdatePublisher){
        return productUpdatePublisher::processor;
    }

}
