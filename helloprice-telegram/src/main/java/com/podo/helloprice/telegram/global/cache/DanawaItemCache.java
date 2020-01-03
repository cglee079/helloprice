package com.podo.helloprice.telegram.global.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.podo.helloprice.core.domain.item.CrawledItemVo;
import com.podo.helloprice.crawler.target.danawa.DanawaCrawler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class DanawaItemCache {

    private final LoadingCache<String, CrawledItemVo> itemCache;

    @Autowired
    public DanawaItemCache(DanawaCrawler danawaCrawler, @Value("${item.cache.expire_minutes}") Integer cacheTimeout) {

        itemCache = Caffeine.newBuilder()
                .maximumSize(10)
                .expireAfterWrite(cacheTimeout, TimeUnit.MINUTES)
                .removalListener((RemovalListener<String, CrawledItemVo>) (itemCode, crawledItemVo, cause) -> {
                    final String itemName = crawledItemVo.getItemName();
                    log.info("{}, '[{}]{}' 상품이 캐쉬에서 삭제됩니다", cause, itemCode, itemName);
                })
                .build(
                        key -> {
                            final CrawledItemVo crawledItemVo = danawaCrawler.crawlItem(key);
                            return crawledItemVo;
                        }
                );

    }

    public CrawledItemVo get(String itemCode) {
        return itemCache.get(itemCode);
    }

}
