package com.podo.helloprice.crawl.agent.global.infra.mq;

import com.podo.helloprice.crawl.agent.job.CrawlProductJobRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MQConfig {

    @Bean
    public Consumer<String> lastCrawledItem(CrawlProductJobRunner crawlProductJobRunner) {
        return (lastPublishedItem) ->{
            log.debug("MQ :: CONSUME :: payload : {}", lastPublishedItem);
            crawlProductJobRunner.run(lastPublishedItem);
        };
    }
}
