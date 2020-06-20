package com.podo.helloprice.product.update.analysis.domain.productsale.exception;

public class InvalidProductSaleByIdException extends RuntimeException {
    public InvalidProductSaleByIdException(Long productId) {
        super("유효하지 않은 상품 판매입니다, 상품 ID : " + productId);
    }
}
