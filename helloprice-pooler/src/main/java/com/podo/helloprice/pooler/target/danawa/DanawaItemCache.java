package com.podo.helloprice.pooler.target.danawa;

import com.podo.helloprice.core.domain.item.ItemInfoVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class DanawaItemCache {

    @Value("${pooler.item.cache.timeout}")
    private Integer cacheTimeout;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    private final Map<String, ItemInfoVo> cache = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> cacheTime = new ConcurrentHashMap<>();

    public void put(String itemCode, ItemInfoVo itemInfoVo) {
        this.cache.put(itemCode, itemInfoVo);
        this.cacheTime.put(itemCode, LocalDateTime.now());

        scheduleRemoveExpire(itemCode);
    }

    private void scheduleRemoveExpire(String itemCode) {
        scheduler.schedule(() -> {
            log.info("DANAWA 상품 캐시가 만료되어 {} 상품을 캐시에서 삭제합니다", itemCode);
            this.cache.remove(itemCode);
            this.cacheTime.remove(itemCode);
        }, cacheTimeout, TimeUnit.MINUTES);
    }

    public ItemInfoVo get(String itemCode) {
        if (has(itemCode)) {
            log.info("DANAWA 상품 캐시에서 {} 상품을 반환합니다", itemCode);
            final ItemInfoVo itemInfoVo = cache.get(itemCode);

            this.cache.remove(itemCode);
            this.cacheTime.remove(itemCode);

            return itemInfoVo;
        }

        return null;
    }

    private boolean has(String itemCode) {
        final LocalDateTime now = LocalDateTime.now();

        if (!cache.containsKey(itemCode)) {
            return false;
        }

        final LocalDateTime lastCacheTime = cacheTime.get(itemCode);

        if (Objects.isNull(lastCacheTime)) {
            return false;
        }

        if (now.minusMinutes(cacheTimeout).compareTo(lastCacheTime) > 0) {
            log.info("DANAWA 상품 캐시에서 {} 상품은 시간이 만료됬습니다", itemCode);
            cache.remove(itemCode);
            cacheTime.remove(itemCode);
            return false;
        }

        log.info("DANAWA 상품 캐시에서 {} 상품을 가지고 있습니다", itemCode);

        return true;
    }


}
