package com.podo.helloprice.api.domain.usernotify.exception;

import com.podo.helloprice.api.global.rest.ApiException;
import com.podo.helloprice.api.global.rest.status.DefaultApiStatus;
import org.springframework.http.HttpStatus;

public class NoNotifiedProductSaleApiException extends ApiException {

    private Long productSaleId;

    public NoNotifiedProductSaleApiException(Long productSaleId) {
        super("판매 알림이 등록되어있지 않은 상품판매 ID 입니다 : " + productSaleId);

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
