package com.podo.helloprice.crawl.scheduler.worker;

import com.podo.helloprice.core.util.JsonUtil;
import com.podo.helloprice.crawl.scheduler.infra.mq.message.LastPublishedProduct;
import com.podo.helloprice.crawl.scheduler.Worker;
import com.podo.helloprice.crawl.scheduler.service.LastCrawledItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.EmitterProcessor;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class LastCrawledItemPublishWorker implements Worker {

    @Value("${product.publish.expire.minute}")
    private Integer crawlExpireMinute;

    private final EmitterProcessor<String> processor;
    private final LastCrawledItemService lastCrawledItemService;

    @Override
    public void run(LocalDateTime now) {
        final LastPublishedProduct lastPublishedProduct = lastCrawledItemService.getLastCrawledItem(now.minusMinutes(crawlExpireMinute));

        if (Objects.isNull(lastPublishedProduct)) {
            return;
        }

        publish(lastPublishedProduct, now);
    }

    private void publish(LastPublishedProduct lastPublishedProduct, LocalDateTime lastPublishAt) {
        try {
            log.debug("MQ :: PUBLISH :: payload : {}", lastPublishedProduct);

            processor.onNext(JsonUtil.toJSON(lastPublishedProduct));
            lastCrawledItemService.updateLastPublishAt(lastPublishedProduct.getProductCode(), lastPublishAt);

        } catch (Exception e) {
            log.error("MQ :: PUBLISH :: ERROR :: {}", lastPublishedProduct, e);
        }
    }

}
