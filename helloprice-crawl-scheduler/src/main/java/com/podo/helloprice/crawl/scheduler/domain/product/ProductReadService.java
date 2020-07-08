package com.podo.helloprice.crawl.scheduler.domain.product;

import com.podo.helloprice.core.enums.ProductAliveStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
@Transactional
@Service
public class ProductReadService {

    private final ProductQuerydslRepository productQuerydslRepository;

    public ProductDto getCrawlProduct(LocalDateTime expirePublishAt){
        final Product product = productQuerydslRepository.findOneByLastCrawledBeforePublishAt(ProductAliveStatus.ALIVE, expirePublishAt);

        if(Objects.isNull(product)){
            return null;
        }

        return new ProductDto(product.getProductName(), product.getProductCode());
    }

    public void updateLastPublishAt(String productCode, LocalDateTime lastPublishAt) {
        final Product product = productQuerydslRepository.findByProductCode(productCode);
        product.updateLastPublishAt(lastPublishAt);
    }
}
