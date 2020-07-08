package com.podo.helloprice.product.update.analysis.domain.productsale.exception;

import com.podo.helloprice.core.enums.SaleType;

public class InvalidProductIdAndSaleTypeException extends RuntimeException {
    public InvalidProductIdAndSaleTypeException(Long productId, SaleType saleType) {
        super("유효하지 않은 상품 판매입니다, 상품 ID : " + productId + ", 판매타입: " + saleType);
    }
}
