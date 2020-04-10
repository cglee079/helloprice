package com.podo.helloprice.crawl.worker.reader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DocumentReaderConfig {

    @Value("${crawler.useragent}")
    private String userAgent;

    @Value("${crawler.timeout}")
    private Long readTimeout;

    @Value("${crawler.webdriver.remotes}")
    private List<String> webDriverRemoteUrls;

    @Bean
    public DocumentPromptReader documentPromptReader() {
        return new DocumentPromptReader(userAgent, readTimeout);
    }

    @Bean
    public DocumentDelayReader documentDelayReader() {
        return new DocumentDelayReader(userAgent, readTimeout, new WebDriverManager(webDriverRemoteUrls));
    }

}
