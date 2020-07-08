package com.podo.helloprice.telegram.domain.productsale.exception;

import com.podo.helloprice.core.enums.SaleType;

public class InvalidProductCodeAndSaleTypeException extends RuntimeException {
    public InvalidProductCodeAndSaleTypeException(String productCode, SaleType saleType) {
        super("유효하지 않은 상품 판매입니다, 상품코드 : " + productCode + ", 판매타입: " + saleType);
    }
}
