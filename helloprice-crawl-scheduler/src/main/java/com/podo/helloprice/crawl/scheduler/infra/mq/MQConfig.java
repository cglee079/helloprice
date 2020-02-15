package com.podo.helloprice.crawl.scheduler.infra.mq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Configuration
public class MQConfig {

    @Bean
    public EmitterProcessor<String> processor() {
        return EmitterProcessor.create();
    }

    @Bean
    public Supplier<Flux<String>> lastCrawledItem() {
        return this::processor;
    }
}
