package com.podo.helloprice.crawl.agent.job.step.crawl;

import com.podo.helloprice.crawl.agent.job.ProductToCrawl;
import com.podo.helloprice.crawl.worker.target.danawa.DanawaProductCrawler;
import com.podo.helloprice.crawl.worker.value.CrawledProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
@StepScope
public class CrawlProductUpdateJobProcessor implements ItemProcessor<ProductToCrawl, CrawledProduct> {

    private final DanawaProductCrawler danawaProductCrawler;

    @Override
    public CrawledProduct process(ProductToCrawl productToCrawl) {
        final String existedProductName = productToCrawl.getProductName();
        final String existedProductCode = productToCrawl.getProductCode();

        log.debug("STEP :: PROCESSOR :: {}({}) 상품의 정보 갱신을 실행합니다", existedProductName, existedProductCode);

        return danawaProductCrawler.crawl(existedProductCode, LocalDateTime.now());
    }
}
