package com.podo.helloprice.crawl.agent.global.infra.mq.consumer;

import com.podo.helloprice.crawl.agent.global.infra.mq.message.CrawlProductMessage;
import com.podo.helloprice.crawl.agent.job.CrawlProductJobRunner;
import com.podo.helloprice.crawl.agent.job.ProductToCrawl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Component
public class CrawlProductJobConsumer implements Consumer<CrawlProductMessage> {

    final CrawlProductJobRunner crawlProductJobRunner;

    @Override
    public void accept(CrawlProductMessage crawlProductMessage) {
        log.debug("MQ :: CONSUME :: payload : {}", crawlProductMessage);

        crawlProductJobRunner.run(new ProductToCrawl(crawlProductMessage.getProductCode(), crawlProductMessage.getProductName()));
    }

}
