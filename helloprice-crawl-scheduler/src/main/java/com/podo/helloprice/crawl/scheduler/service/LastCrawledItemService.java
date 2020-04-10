package com.podo.helloprice.crawl.scheduler.service;

import com.podo.helloprice.code.model.ProductAliveStatus;
import com.podo.helloprice.crawl.scheduler.domain.product.Product;
import com.podo.helloprice.crawl.scheduler.domain.product.ProductQuerydslRepository;
import com.podo.helloprice.crawl.scheduler.infra.mq.message.LastPublishedProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
@Transactional
@Service
public class LastCrawledItemService {

    private final ProductQuerydslRepository productQuerydslRepository;

    public LastPublishedProduct getLastCrawledItem(LocalDateTime expirePublishAt){
        final Product product = productQuerydslRepository.findOneByLastCrawledBeforePublishAt(ProductAliveStatus.ALIVE, expirePublishAt);

        if(Objects.isNull(product)){
            return null;
        }

        return new LastPublishedProduct(product.getProductName(), product.getProductCode());
    }

    public void updateLastPublishAt(String itemCode, LocalDateTime lastPublishAt) {
        final Product product = productQuerydslRepository.findByItemCode(itemCode);
        product.updateLastPublishAt(lastPublishAt);
    }
}
