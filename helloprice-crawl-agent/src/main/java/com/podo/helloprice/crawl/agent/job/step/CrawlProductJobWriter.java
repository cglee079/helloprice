package com.podo.helloprice.crawl.agent.job.step;

import com.podo.helloprice.crawl.agent.domain.product.Product;
import com.podo.helloprice.crawl.agent.domain.product.ProductRepository;
import com.podo.helloprice.crawl.agent.global.infra.mq.NotifyEventPublisher;
import com.podo.helloprice.crawl.agent.global.infra.mq.message.LastPublishedProduct;
import com.podo.helloprice.crawl.agent.global.infra.mq.message.NotifyEvent;
import com.podo.helloprice.crawl.agent.global.infra.mq.message.UpdateStatus;
import com.podo.helloprice.crawl.agent.job.CrawlProductJobParameter;
import com.podo.helloprice.crawl.worker.vo.CrawledProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.EmitterProcessor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class CrawlProductJobWriter implements ItemWriter<CrawledProduct> {


    @Value("${product.max_dead_count}")
    private Integer maxDeadCount;

    private final CrawlProductJobParameter crawlProductJobParameter;
    private final ProductRepository productRepository;
    private final NotifyEventPublisher notifyEventPublisher;

    @Override
    public void write(List<? extends CrawledProduct> crawledProducts) {
        for (CrawledProduct crawledProduct : crawledProducts) {
            updateProduct(crawledProduct);
        }
    }

    private void updateProduct(CrawledProduct crawledProduct) {
        final LocalDateTime now = LocalDateTime.now();
        final LastPublishedProduct lastPublishedProduct = crawlProductJobParameter.getLastPublishedProduct();
        final String productName = lastPublishedProduct.getProductName();
        final String productCode = lastPublishedProduct.getProductCode();
        final Product product = productRepository.findByProductCode(productCode);
        final Long id = product.getId();

        if (Objects.isNull(crawledProduct)) {
            log.debug("STEP :: WRITER :: {}({}) :: 상품의 정보 갱신 에러 발생", productName, productCode);
            if(product.increaseDeadCount(maxDeadCount, now)) {
                notifyEventPublisher.publish(new NotifyEvent(id, UpdateStatus.UPDATE_DEAD));
            }
            return;
        }

        for (UpdateStatus updateStatus : product.updateByCrawledProduct(crawledProduct)) {
            notifyEventPublisher.publish(new NotifyEvent(id, updateStatus));
        };

        log.debug("STEP :: WRITER :: {}({}) ::  상품판매상태 : `{}`, 상품상태 `{}`", productName, productCode, product.getSaleStatus().getValue(), product.getAliveStatus());
    }


}
