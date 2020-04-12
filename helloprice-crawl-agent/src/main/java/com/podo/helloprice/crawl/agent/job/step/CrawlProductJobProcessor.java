package com.podo.helloprice.crawl.agent.job.step;

import com.podo.helloprice.crawl.agent.global.infra.mq.message.LastPublishedProduct;
import com.podo.helloprice.crawl.worker.target.danawa.DanawaProductCrawler;
import com.podo.helloprice.crawl.worker.vo.CrawledProduct;
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
public class CrawlProductJobProcessor implements ItemProcessor<LastPublishedProduct, CrawledProduct> {

    private final DanawaProductCrawler danawaProductCrawler;

    @Override
    public CrawledProduct process(LastPublishedProduct lastPublishedProduct) {
        final String existedProductName = lastPublishedProduct.getProductName();
        final String existedProductCode = lastPublishedProduct.getProductCode();

        log.debug("STEP :: PROCESSOR :: {}({}) 상품의 정보 갱신을 실행합니다", existedProductName, existedProductCode);

        return danawaProductCrawler.crawl(existedProductCode, LocalDateTime.now());
    }
}
