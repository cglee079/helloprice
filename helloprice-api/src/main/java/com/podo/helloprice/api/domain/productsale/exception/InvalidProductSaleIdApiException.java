package com.podo.helloprice.api.domain.productsale.exception;

import com.podo.helloprice.api.global.rest.ApiException;
import com.podo.helloprice.api.global.rest.status.DefaultApiStatus;
import org.springframework.http.HttpStatus;

public class InvalidProductSaleIdApiException extends ApiException {

    private Long productSaleId;

    public InvalidProductSaleIdApiException(Long productSaleId) {
        super("찾을 수 없는 상품 판매 ID 입니다 : " + productSaleId);
        this.productSaleId = productSaleId;
    }

    @Override
    public String getField() {
        return "productSaleId";
    }

    @Override
    public Object getValue() {
        return productSaleId;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public DefaultApiStatus getApiStatus() {
        return DefaultApiStatus.ERR_INVALID;
    }
}
