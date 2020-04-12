package com.podo.helloprice.crawl.worker.target.danawa.config;

import com.podo.helloprice.crawl.worker.reader.DocumentDelayReader;
import com.podo.helloprice.crawl.worker.target.danawa.DanawaProductCodeCrawler;
import com.podo.helloprice.crawl.worker.target.danawa.DanawaProductSearchCrawler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DanawaDelayCrawlerConfig {

    @Bean
    @ConditionalOnBean(name = "documentDelayReader")
    public DanawaProductSearchCrawler danawaProductSearchCrawler(
            DocumentDelayReader documentDelayReader,
            DanawaProductCodeCrawler danawaProductCodeCrawler){

        return new DanawaProductSearchCrawler(documentDelayReader, danawaProductCodeCrawler);
    }
}
