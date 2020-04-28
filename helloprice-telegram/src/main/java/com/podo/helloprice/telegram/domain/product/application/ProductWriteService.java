package com.podo.helloprice.telegram.domain.product.application;

import com.podo.helloprice.core.enums.PriceType;
import com.podo.helloprice.crawl.worker.value.CrawledProduct;
import com.podo.helloprice.crawl.worker.value.CrawledProductPrice;
import com.podo.helloprice.telegram.domain.product.dto.ProductInsertDto;
import com.podo.helloprice.telegram.domain.product.model.Product;
import com.podo.helloprice.telegram.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.podo.helloprice.core.enums.PriceType.*;
import static com.podo.helloprice.telegram.domain.product.model.ProductPrice.create;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ProductWriteService {

    private final ProductRepository productRepository;

    public Long writeCrawledProduct(@NotNull CrawledProduct crawledProduct) {
        final LocalDateTime crawledAt = crawledProduct.getCrawledAt();

        final Optional<Product> existedProductOptional = productRepository.findByProductCode(crawledProduct.getProductCode());
        if (existedProductOptional.isPresent()) {
            final Product existedProduct = existedProductOptional.get();
            existedProduct.updateByCrawledProduct(crawledProduct);
            return existedProduct.getId();
        }

        final ProductInsertDto productInsert = ProductInsertDto.builder()
                .productName(crawledProduct.getProductName())
                .productCode(crawledProduct.getProductCode())
                .url(crawledProduct.getUrl())
                .description(crawledProduct.getDescription())
                .imageUrl(crawledProduct.getImageUrl())
                .saleStatus(crawledProduct.getSaleStatus())
                .build();

        final Product savedProduct = productRepository.save(productInsert.toEntity());

        for (PriceType priceType : values()) {
            final CrawledProductPrice crawledProductPrice = crawledProduct.getProductPriceByType(priceType);

            if (Objects.nonNull(crawledProductPrice)) {
                savedProduct.addProductPrice(create(priceType, crawledProductPrice.getPrice(), crawledProductPrice.getAdditionalInfo(), crawledAt));
            }
        }

        return savedProduct.getId();
    }


}
