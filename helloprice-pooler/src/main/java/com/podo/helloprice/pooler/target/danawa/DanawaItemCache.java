package com.podo.helloprice.pooler.target.danawa;

import com.podo.helloprice.core.domain.item.ItemInfoVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class DanawaItemCache {

    @Value("${pooler.item.cache.timeout}")
    private Integer cacheTimeout;

    private final Map<String, ItemInfoVo> cache = new HashMap<>();
    private final Map<String, LocalDateTime> cacheTime = new HashMap<>();

    public void put(String itemCode, ItemInfoVo itemInfoVo) {
        this.cache.put(itemCode, itemInfoVo);
        this.cacheTime.put(itemCode, LocalDateTime.now());
    }

    public ItemInfoVo get(String itemCode) {
        if (has(itemCode)) {
            log.info("DANAWA 상품 캐시에서 {} 상품을 반환합니다", itemCode);
            return cache.get(itemCode);
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
            cache.remove(itemCode);
            cacheTime.remove(itemCode);
            return false;
        }

        log.info("DANAWA 상품 캐시에서 {} 상품을 가지고 있습니다", itemCode);

        return true;
    }


}
