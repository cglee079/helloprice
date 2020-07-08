package com.podo.helloprice.crawl.scheduler.job.worker;

import com.podo.helloprice.crawl.scheduler.domain.product.ProductDto;
import com.podo.helloprice.crawl.scheduler.global.config.infra.mq.publisher.ProductToCrawlPublisher;
import com.podo.helloprice.crawl.scheduler.global.config.infra.mq.message.ProductToCrawlMessage;
import com.podo.helloprice.crawl.scheduler.domain.product.ProductReadService;
import com.podo.helloprice.crawl.scheduler.job.Worker;
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

    private final ProductToCrawlPublisher productToCrawlPublisher;
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
        productToCrawlPublisher.publish(new ProductToCrawlMessage(crawlProduct.getProductName(), crawlProduct.getProductCode()));
        productReadService.updateLastPublishAt(crawlProduct.getProductCode(), lastPublishAt);
    }
}
