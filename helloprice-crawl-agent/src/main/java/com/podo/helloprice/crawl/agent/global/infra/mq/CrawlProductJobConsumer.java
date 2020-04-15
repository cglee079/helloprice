package com.podo.helloprice.crawl.agent.global.infra.mq;

import com.podo.helloprice.crawl.agent.global.infra.mq.message.LastPublishedProduct;
import com.podo.helloprice.crawl.agent.job.CrawlProductJobRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Component
public class CrawlProductJobConsumer implements Consumer<LastPublishedProduct> {

    final CrawlProductJobRunner crawlProductJobRunner;

    @Override
    public void accept(LastPublishedProduct lastPublishedProduct) {
        log.debug("MQ :: CONSUME :: payload : {}", lastPublishedProduct);
        crawlProductJobRunner.run(lastPublishedProduct);
    }

}
