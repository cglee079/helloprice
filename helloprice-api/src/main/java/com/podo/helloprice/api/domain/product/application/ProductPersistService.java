package com.podo.helloprice.api.domain.product.application;

import com.podo.helloprice.api.domain.product.dto.ProductPersistDto;
import com.podo.helloprice.api.domain.product.model.Product;
import com.podo.helloprice.api.domain.product.repository.ProductRepository;
import com.podo.helloprice.api.domain.productsale.ProductSale;
import com.podo.helloprice.api.domain.productsale.application.ProductSaleWriteService;
import com.podo.helloprice.api.domain.productsale.repository.ProductSaleRepository;
import com.podo.helloprice.core.enums.ProductAliveStatus;
import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.crawl.worker.target.danawa.DanawaProductCrawler;
import com.podo.helloprice.crawl.worker.value.CrawledProduct;
import com.podo.helloprice.crawl.worker.value.CrawledProductPrice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;


@Transactional
@RequiredArgsConstructor
@Service
public class ProductPersistService {

    private final ProductSaleWriteService productSaleWriteService;
    private final DanawaProductCrawler danawaProductCrawler;
    private final ProductRepository productRepository;
    private final ProductSaleRepository productSaleRepository;

    public Long persist(ProductPersistDto productWrite, LocalDateTime now) {

        final String productCode = productWrite.getProductCode();

        final Optional<Product> existedProduct = productRepository.findByProductCode(productCode);
        final CrawledProduct crawledProduct = danawaProductCrawler.crawl(productCode, now);

        if (existedProduct.isPresent()) {
            updateProduct(existedProduct.get(), crawledProduct);
            return existedProduct.get().getId();
        }


        final Product product = Product.builder()
                .productName(crawledProduct.getProductName())
                .productCode(crawledProduct.getProductCode())
                .url(crawledProduct.getUrl())
                .description(crawledProduct.getDescription())
                .imageUrl(crawledProduct.getImageUrl())
                .saleStatus(crawledProduct.getSaleStatus())
                .aliveStatus(ProductAliveStatus.ALIVE)
                .build();

        final Product savedProduct = productRepository.save(product);

        for (SaleType saleType : SaleType.values()) {
            final CrawledProductPrice crawledProductPrice = crawledProduct.getProductPriceByType(saleType);

            if (Objects.nonNull(crawledProductPrice)) {
                productSaleRepository.save(ProductSale.create(savedProduct, saleType, crawledProductPrice.getPrice(), crawledProductPrice.getAdditionalInfo(), now));
            }
        }

        return savedProduct.getId();
    }

    private Long updateProduct(Product product, @NotNull CrawledProduct crawledProduct) {
        product.updateByCrawledProduct(crawledProduct);

        final Long productId = product.getId();

        switch (crawledProduct.getSaleStatus()) {
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
