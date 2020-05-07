package com.podo.helloprice.telegram.domain.product.application;

import com.podo.helloprice.telegram.domain.product.application.helper.ProductReadHelper;
import com.podo.helloprice.telegram.domain.product.dto.ProductDto;
import com.podo.helloprice.telegram.domain.product.model.Product;
import com.podo.helloprice.telegram.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ProductReadService {

    private final ProductRepository productRepository;

    public ProductDto findByProductId(Long productId) {
        final Product product = ProductReadHelper.findProductById(productRepository, productId);
        return new ProductDto(product);
    }

    public boolean isExistedByProductParameter(String productCode) {
        return productRepository.findByProductCode(productCode).isPresent();
    }

}
