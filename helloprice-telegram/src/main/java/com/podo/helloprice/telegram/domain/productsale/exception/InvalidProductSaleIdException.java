package com.podo.helloprice.telegram.domain.productsale.exception;

public class InvalidProductSaleIdException extends RuntimeException {
    public InvalidProductSaleIdException(Long id) {

            super("유효하지 않는 상품판매 ID 입니다. Id : " + id);
    }
}
