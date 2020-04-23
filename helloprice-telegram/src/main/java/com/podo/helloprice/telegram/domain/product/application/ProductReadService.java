package com.podo.helloprice.telegram.domain.product.application;

import com.podo.helloprice.core.enums.PriceType;
import com.podo.helloprice.telegram.domain.product.application.helper.ProductReadHelper;
import com.podo.helloprice.telegram.domain.product.dto.ProductAllPriceTypeDto;
import com.podo.helloprice.telegram.domain.product.model.Product;
import com.podo.helloprice.telegram.domain.product.dto.ProductOnePriceTypeDto;
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
public class ProductReadService {

    private final ProductRepository productRepository;

    public ProductAllPriceTypeDto findByProductId(Long productId) {
        final Product product = ProductReadHelper.findProductById(productRepository, productId);
        return new ProductAllPriceTypeDto(product);
    }

    public ProductOnePriceTypeDto findByProductId(Long productId, PriceType priceType) {
        final Product product = ProductReadHelper.findProductById(productRepository, productId);
        return new ProductOnePriceTypeDto(product, priceType);
    }

    public boolean isExistedByProductParameter(String productCode) {
        return productRepository.findByProductCode(productCode).isPresent();
    }

    public ProductOnePriceTypeDto findByProductParameter(String productCode, PriceType priceType) {
        final Optional<Product> existedProduct = productRepository.findByProductCode(productCode);
        return existedProduct.map(p -> new ProductOnePriceTypeDto(p, priceType)).orElse(null);
    }
}
