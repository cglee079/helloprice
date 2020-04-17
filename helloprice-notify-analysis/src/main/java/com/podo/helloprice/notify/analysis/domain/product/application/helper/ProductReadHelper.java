package com.podo.helloprice.notify.analysis.domain.product.application.helper;

import com.podo.helloprice.notify.analysis.domain.product.exception.InvalidProductIdException;
import com.podo.helloprice.notify.analysis.domain.product.model.Product;
import com.podo.helloprice.notify.analysis.domain.product.repository.ProductRepository;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductReadHelper {

    public static Product findProductById(ProductRepository productRepository, Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new InvalidProductIdException(productId));
    }

}
