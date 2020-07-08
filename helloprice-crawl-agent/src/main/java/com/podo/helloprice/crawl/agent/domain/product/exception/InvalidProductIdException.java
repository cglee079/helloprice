package com.podo.helloprice.crawl.agent.domain.product.exception;

public class InvalidProductIdException extends RuntimeException{
    public InvalidProductIdException(Long id) {
        super("유효하지 않는 상품 ID 입니다. Id : " + id);
    }

}
