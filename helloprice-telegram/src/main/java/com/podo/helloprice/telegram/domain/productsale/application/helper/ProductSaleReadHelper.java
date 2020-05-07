package com.podo.helloprice.telegram.domain.productsale.application.helper;

import com.podo.helloprice.telegram.domain.productsale.ProductSale;
import com.podo.helloprice.telegram.domain.productsale.ProductSaleRepository;
import com.podo.helloprice.telegram.domain.productsale.exception.InvalidProductSaleIdException;

public class ProductSaleReadHelper {
    public static ProductSale findProductSaleById(ProductSaleRepository productSaleRepository, Long productSaleId) {
        return productSaleRepository.findById(productSaleId)
                .orElseThrow(() -> new InvalidProductSaleIdException(productSaleId));
    }
}
