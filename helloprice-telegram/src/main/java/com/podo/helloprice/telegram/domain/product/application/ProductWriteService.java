package com.podo.helloprice.telegram.domain.product.application;

import com.podo.helloprice.crawl.worker.vo.CrawledProduct;
import com.podo.helloprice.telegram.domain.product.Product;
import com.podo.helloprice.telegram.domain.product.application.helper.ProductReadHelper;
import com.podo.helloprice.telegram.domain.product.dto.ProductInsertDto;
import com.podo.helloprice.telegram.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ProductWriteService {

    private final ProductRepository productRepository;

    public Long writeCrawledProduct(CrawledProduct crawledProduct) {

        final Optional<Product> byProductCode = productRepository.findByProductCode(crawledProduct.getProductCode());

        if (byProductCode.isPresent()) {
            final Product existedProduct = byProductCode.get();
            existedProduct.updateByCrawledProduct(crawledProduct);
            return existedProduct.getId();
        }

        final ProductInsertDto productInsert = ProductInsertDto.builder()
                .productName(crawledProduct.getProductName())
                .productCode(crawledProduct.getProductCode())
                .url(crawledProduct.getUrl())
                .description(crawledProduct.getDescription())
                .imageUrl(crawledProduct.getImageUrl())
                .price(crawledProduct.getPrice())
                .saleStatus(crawledProduct.getSaleStatus())
                .build();

        return insertNewProduct(productInsert);
    }

    public Long insertNewProduct(ProductInsertDto productInsert) {
        final Product savedProduct = productRepository.save(productInsert.toEntity());
        return savedProduct.getId();
    }

    public void notifiedProduct(Long productId) {
        final Product product = ProductReadHelper.findProductById(productRepository, productId);
        product.notified();
    }
}
