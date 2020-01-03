package com.podo.helloprice.telegram.domain.item.exception;

public class InvalidItemIdException extends RuntimeException {

    public InvalidItemIdException(Long id) {
        super("유효하지 않는 상품 ID 입니다. Id : " + id);
    }
}
