package com.podo.helloprice.telegram.domain.product.exception;

public class InvalidProductCodeException extends RuntimeException {
    public InvalidProductCodeException(String productCode) {
        super("유효하지 않는 상품코드 입니다. Code : " + productCode);
    }
}
