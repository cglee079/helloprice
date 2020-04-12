package com.podo.helloprice.crawl.worker.reader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class DocumentReaderConfig {

    @Bean
    public DocumentPromptReader documentPromptReader(DocumentReaderProperties properties) {
        final String useragent = properties.getUseragent();
        final Long readTimeout = properties.getReadTimeout();

        log.debug("CRAWLER :: PROMPT READER : userAgent : {}, readTimeout : {}", useragent, readTimeout);

        return new DocumentPromptReader(useragent, readTimeout);
    }

    @Bean
    public DocumentDelayReader documentDelayReader(DocumentReaderProperties properties) {
        final String useragent = properties.getUseragent();
        final Long readTimeout = properties.getReadTimeout();
        final List<String> webdriverRemotes = properties.getWebdriverRemotes();

        if (Objects.isNull(webdriverRemotes) || webdriverRemotes.isEmpty()) {
            log.warn("CRAWLER :: WEB Driver 가 설정되지 않았습니다 : `crawler.webdriver-remotes`");
            return null;
        }

        log.debug("CRAWLER :: DELAY READER : userAgent : {}, readTimeout : {}, webdriver-remotes : {}", useragent, readTimeout, webdriverRemotes);

        return new DocumentDelayReader(
                useragent,
                readTimeout,
                new WebDriverManager(properties.getWebdriverRemotes())
        );
    }

}
