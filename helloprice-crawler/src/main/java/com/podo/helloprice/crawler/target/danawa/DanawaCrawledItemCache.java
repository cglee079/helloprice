package com.podo.helloprice.crawler.target.danawa;

import com.podo.helloprice.core.domain.item.CrawledItemVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class DanawaCrawledItemCache {

    @Value("${crawler.item.cache.timeout}")
    private Integer cacheTimeout;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    private final Map<String, CrawledItemVo> itemCache = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> itemCacheTime = new ConcurrentHashMap<>();

    public void put(String itemCode, CrawledItemVo crawledItemVo) {
        this.itemCache.put(itemCode, crawledItemVo);
        this.itemCacheTime.put(itemCode, LocalDateTime.now());

        scheduleRemoveExpire(itemCode);
    }

    private void scheduleRemoveExpire(String itemCode) {
        scheduler.schedule(() -> {
            log.info("DANAWA 상품 캐시가 만료되어 {} 상품을 캐시에서 삭제합니다", itemCode);
            this.itemCache.remove(itemCode);
            this.itemCacheTime.remove(itemCode);
        }, cacheTimeout, TimeUnit.MINUTES);
    }

    public CrawledItemVo get(String itemCode) {
        if (has(itemCode)) {
            log.info("DANAWA 상품 캐시에서 {} 상품을 반환합니다", itemCode);
            final CrawledItemVo crawledItemVo = itemCache.get(itemCode);

            this.itemCache.remove(itemCode);
            this.itemCacheTime.remove(itemCode);

            return crawledItemVo;
        }

        return null;
    }

    private boolean has(String itemCode) {
        final LocalDateTime now = LocalDateTime.now();

        if (!itemCache.containsKey(itemCode)) {
            return false;
        }

        final LocalDateTime lastCacheTime = itemCacheTime.get(itemCode);

        if (Objects.isNull(lastCacheTime)) {
            return false;
        }

        if (now.minusMinutes(cacheTimeout).compareTo(lastCacheTime) > 0) {
            log.info("DANAWA 상품 캐시에서 {} 상품은 시간이 만료됬습니다", itemCode);
            itemCache.remove(itemCode);
            itemCacheTime.remove(itemCode);
            return false;
        }

        log.info("DANAWA 상품 캐시에서 {} 상품을 가지고 있습니다", itemCode);

        return true;
    }


}
