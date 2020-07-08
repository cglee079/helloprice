package com.podo.helloprice.telegram.domain.product.application;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.crawl.worker.value.CrawledProduct;
import com.podo.helloprice.crawl.worker.value.CrawledProductPrice;
import com.podo.helloprice.telegram.domain.product.dto.ProductInsertDto;
import com.podo.helloprice.telegram.domain.product.model.Product;
import com.podo.helloprice.telegram.domain.product.repository.ProductRepository;
import com.podo.helloprice.telegram.domain.productsale.ProductSale;
import com.podo.helloprice.telegram.domain.productsale.ProductSaleRepository;
import com.podo.helloprice.telegram.domain.productsale.application.ProductSaleWriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.podo.helloprice.core.enums.SaleType.values;
import static com.podo.helloprice.telegram.domain.productsale.ProductSale.create;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ProductWriteService {

    private final ProductSaleWriteService productSaleWriteService;
    private final ProductRepository productRepository;
    private final ProductSaleRepository productSaleRepository;

    public Long writeCrawledProduct(@NotNull CrawledProduct crawledProduct) {
        final LocalDateTime crawledAt = crawledProduct.getCrawledAt();

        final Optional<Product> existedProductOptional = productRepository.findByProductCode(crawledProduct.getProductCode());
        if (existedProductOptional.isPresent()) {
            return updateProduct(existedProductOptional.get(), crawledProduct);
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

        for (SaleType saleType : values()) {
            final CrawledProductPrice crawledProductPrice = crawledProduct.getProductPriceByType(saleType);

            if (Objects.nonNull(crawledProductPrice)) {
                final ProductSale productSale = productSaleRepository.save(create(saleType, crawledProductPrice.getPrice(), crawledProductPrice.getAdditionalInfo(), crawledAt));
                savedProduct.putProductSale(productSale.getSaleType(), productSale);
            }
        }

        return savedProduct.getId();
    }

    private Long updateProduct(Product product, @NotNull CrawledProduct crawledProduct) {
        product.updateByCrawledProduct(crawledProduct);

        final Long productId = product.getId();

        switch (crawledProduct.getSaleStatus()){
            case UNKNOWN:
            case DISCONTINUE:
            case NOT_SUPPORT:
            case EMPTY_AMOUNT:
                productSaleWriteService.setPriceZeroByProductId(productId, crawledProduct.getCrawledAt());
                break;
            case SALE:
                productSaleWriteService.updateSalePrice(productId, crawledProduct.getSaleTypeToPrice(), crawledProduct.getCrawledAt());
            default:

        }
        return productId;
    }


}
