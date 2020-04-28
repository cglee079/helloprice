package com.podo.helloprice.crawl.agent.job.step.crawl;

import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.core.parser.ProductSaleStatusParser;
import com.podo.helloprice.crawl.agent.domain.product.Product;
import com.podo.helloprice.crawl.agent.domain.product.ProductRepository;
import com.podo.helloprice.crawl.agent.job.CrawlProductJobParameter;
import com.podo.helloprice.crawl.agent.job.ProductUpdateEventStore;
import com.podo.helloprice.crawl.agent.job.ProductToCrawl;
import com.podo.helloprice.crawl.agent.job.ProductUpdateEvent;
import com.podo.helloprice.crawl.worker.value.CrawledProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class CrawlProductUpdateJobWriter implements ItemWriter<CrawledProduct> {


    @Value("${product.max_dead_count}")
    private Integer maxDeadCount;

    private final CrawlProductJobParameter crawlProductJobParameter;
    private final ProductRepository productRepository;
    private final ProductUpdateEventStore productUpdateEventStore;

    @Override
    public void write(List<? extends CrawledProduct> crawledProducts) {
        for (CrawledProduct crawledProduct : crawledProducts) {
            updateProduct(crawledProduct);
        }
    }

    private void updateProduct(CrawledProduct crawledProduct) {
        final LocalDateTime now = LocalDateTime.now();
        final ProductToCrawl productToCrawl = crawlProductJobParameter.getProductToCrawl();
        final String productName = productToCrawl.getProductName();
        final String productCode = productToCrawl.getProductCode();
        final Product product = productRepository.findByProductCode(productCode);
        final Long productId = product.getId();

        if (CrawledProduct.FAIL.equals(crawledProduct)) {
            log.debug("STEP :: WRITER :: {}({}) :: 상품의 정보 갱신 에러 발생", productName, productCode);
            if(product.increaseDeadCount(maxDeadCount, now)) {
                productUpdateEventStore.addProductUpdateEvent(new ProductUpdateEvent(productId, ProductUpdateStatus.UPDATE_DEAD));
            }
            return;
        }

        for (ProductUpdateStatus productUpdateStatus : product.updateByCrawledProduct(crawledProduct)) {
            productUpdateEventStore.addProductUpdateEvent(new ProductUpdateEvent(productId, productUpdateStatus));
        }

        log.debug("STEP :: WRITER :: {}({}) ::  상품상태 : `{}`, 상품판매상태 `{}`", productName, productCode, product.getAliveStatus(), ProductSaleStatusParser.kr(product.getSaleStatus()));
    }


}
