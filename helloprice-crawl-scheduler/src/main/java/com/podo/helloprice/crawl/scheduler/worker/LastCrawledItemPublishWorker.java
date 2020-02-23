package com.podo.helloprice.crawl.scheduler.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.podo.helloprice.crawl.core.vo.LastPublishedItem;
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
@Component
@RequiredArgsConstructor
public class LastCrawledItemPublishWorker implements Worker {

    @Value("${item.publish.expire.minute}")
    private Integer crawlExpireMinute;

    private final EmitterProcessor<String> processor;
    private final LastCrawledItemService lastCrawledItemService;
    private final ObjectMapper objectMapper;

    @Override
    public void run() {
        final LocalDateTime now = LocalDateTime.now();

        final LastPublishedItem lastPublishedItem = lastCrawledItemService.getLastCrawledItem(now.minusMinutes(crawlExpireMinute));

        if (Objects.isNull(lastPublishedItem)) {
            return;
        }

        publish(lastPublishedItem, now);
    }

    private void publish(LastPublishedItem lastPublishedItem, LocalDateTime lastPublishAt) {
        try {
            log.info("메세지 전송 : {}", lastPublishedItem);
            processor.onNext(toMessage(lastPublishedItem));

            lastCrawledItemService.updateLastPublishAt(lastPublishedItem.getItemCode(), lastPublishAt);
        } catch (Exception e) {
            log.error("Fail Publish {}", lastPublishedItem, e);
        }
    }

    private String toMessage(LastPublishedItem lastPublishedItem) throws Exception {
        return objectMapper.writeValueAsString(lastPublishedItem);
    }
}
