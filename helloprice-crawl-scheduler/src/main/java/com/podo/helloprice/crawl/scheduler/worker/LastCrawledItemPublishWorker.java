package com.podo.helloprice.crawl.scheduler.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.podo.helloprice.crawl.core.vo.LastCrawledItem;
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

        final LastCrawledItem lastCrawledItem = lastCrawledItemService.getLastCrawledItem(now.minusMinutes(crawlExpireMinute));

        if (Objects.isNull(lastCrawledItem)) {
            return;
        }

        publish(lastCrawledItem, now);
    }

    private void publish(LastCrawledItem lastCrawledItem, LocalDateTime lastPublishAt) {
        try {
            log.info("메세지 전송 : {}", lastCrawledItem);
            processor.onNext(toMessage(lastCrawledItem));

            lastCrawledItemService.updateLastPublishAt(lastCrawledItem.getItemCode(), lastPublishAt);
        } catch (Exception e) {
            log.error("Fail Publish {}", lastCrawledItem, e);
        }
    }

    private String toMessage(LastCrawledItem lastCrawledItem) throws Exception {
        return objectMapper.writeValueAsString(lastCrawledItem);
    }
}
