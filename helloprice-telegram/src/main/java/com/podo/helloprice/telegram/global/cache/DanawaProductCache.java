package com.podo.helloprice.telegram.global.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.podo.helloprice.crawl.worker.target.danawa.DanawaProductCrawler;
import com.podo.helloprice.crawl.worker.value.CrawledProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class DanawaProductCache {

    private final LoadingCache<String, CrawledProduct> productCache;

    @Autowired
    public DanawaProductCache(DanawaProductCrawler danawaProductCrawler, @Value("${product.cache.expire_minutes}") Integer cacheTimeout) {
        productCache = Caffeine.newBuilder()
                .maximumSize(10)
                .expireAfterWrite(cacheTimeout, TimeUnit.MINUTES)
                .removalListener((RemovalListener<String, CrawledProduct>) (productCode, crawledProduct, cause) -> {
                    log.debug("CACHE :: {}, '[{}]{}' 상품이 캐쉬에서 삭제됩니다", cause, productCode, crawledProduct.getProductName());
                })
                .build(key -> danawaProductCrawler.crawl(key, LocalDateTime.now()));
    }

    public CrawledProduct get(String productCode) {
        return productCache.get(productCode);
    }

}
