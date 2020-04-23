package com.podo.helloprice.telegram.domain.product.application.helper;

import com.podo.helloprice.telegram.domain.product.exception.InvalidProductCodeException;
import com.podo.helloprice.telegram.domain.product.model.Product;
import com.podo.helloprice.telegram.domain.product.exception.InvalidProductIdException;
import com.podo.helloprice.telegram.domain.product.repository.ProductRepository;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductReadHelper {

    public static Product findProductById(ProductRepository productRepository, Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new InvalidProductIdException(productId));
    }

}
