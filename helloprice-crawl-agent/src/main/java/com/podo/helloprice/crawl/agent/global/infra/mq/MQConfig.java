package com.podo.helloprice.crawl.agent.global.infra.mq;

import com.podo.helloprice.crawl.agent.job.CrawlJobRunner;
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
    public Consumer<String> lastCrawledItem(CrawlJobRunner crawlJobRunner) {
        return (lastCrawledItem) ->{
            log.info("메세지 수신 : " + lastCrawledItem);
            crawlJobRunner.run(lastCrawledItem);
        };
    }
}
