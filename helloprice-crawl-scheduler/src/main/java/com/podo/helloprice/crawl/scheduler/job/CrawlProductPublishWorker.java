package com.podo.helloprice.crawl.scheduler.job;

import com.podo.helloprice.crawl.scheduler.domain.product.ProductDto;
import com.podo.helloprice.crawl.scheduler.global.config.infra.mq.CrawlProductPublisher;
import com.podo.helloprice.crawl.scheduler.global.config.infra.mq.message.CrawlProductMessage;
import com.podo.helloprice.crawl.scheduler.domain.product.ProductReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class CrawlProductPublishWorker implements Worker {

    @Value("${product.publish.expire.minute}")
    private Integer crawlExpireMinute;

    private final CrawlProductPublisher crawlProductPublisher;
    private final ProductReadService productReadService;

    @Override
    public void run(LocalDateTime now) {
        final ProductDto crawlProduct = productReadService.getCrawlProduct(now.minusMinutes(crawlExpireMinute));

        if (Objects.isNull(crawlProduct)) {
            return;
        }

        publish(crawlProduct, now);
    }

    private void publish(ProductDto crawlProduct, LocalDateTime lastPublishAt) {
        crawlProductPublisher.publish(new CrawlProductMessage(crawlProduct.getProductName(), crawlProduct.getProductCode()));
        productReadService.updateLastPublishAt(crawlProduct.getProductCode(), lastPublishAt);
    }
}
