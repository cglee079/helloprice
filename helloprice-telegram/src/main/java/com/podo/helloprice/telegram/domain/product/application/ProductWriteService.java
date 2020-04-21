package com.podo.helloprice.telegram.domain.product.application;

import com.podo.helloprice.crawl.worker.vo.CrawledProduct;
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

import static com.podo.helloprice.core.model.PriceType.*;
import static com.podo.helloprice.telegram.domain.product.model.ProductPrice.create;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ProductWriteService {

    private final ProductRepository productRepository;

    public Long writeCrawledProduct(@NotNull CrawledProduct crawledProduct) {
        final LocalDateTime crawledAt = crawledProduct.getCrawledAt();

        final Optional<Product> byProductCode = productRepository.findByProductCode(crawledProduct.getProductCode());

        if (byProductCode.isPresent()) {
            return updateExistedProduct(crawledProduct, byProductCode.get());
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

        savedProduct.addProductPrice(create(NORMAL, crawledProduct.getPrice(), null, crawledAt));

        if(Objects.nonNull(crawledProduct.getCashPrice())){
            savedProduct.addProductPrice(create(CASH, crawledProduct.getCashPrice(), null, crawledAt));
        }

        if(Objects.nonNull(crawledProduct.getCardPrice())){
            savedProduct.addProductPrice(create(CARD, crawledProduct.getCardPrice(), crawledProduct.getCardType(), crawledAt));
        }

        return savedProduct.getId();
    }

    private Long updateExistedProduct(@NotNull CrawledProduct crawledProduct, Product existedProduct) {
        existedProduct.updateByCrawledProduct(crawledProduct);

        return existedProduct.getId();
    }

}
