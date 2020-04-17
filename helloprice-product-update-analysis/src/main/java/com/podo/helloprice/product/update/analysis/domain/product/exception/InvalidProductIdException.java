package com.podo.helloprice.product.update.analysis.domain.product.exception;

public class InvalidProductIdException extends RuntimeException {
    public InvalidProductIdException(Long productId) {
        super("찾을수 없는 상품 ID 입니다 : " + productId);
    }
}
