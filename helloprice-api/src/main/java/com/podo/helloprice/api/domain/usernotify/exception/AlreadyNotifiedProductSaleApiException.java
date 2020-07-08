package com.podo.helloprice.api.domain.usernotify.exception;

import com.podo.helloprice.api.global.rest.ApiException;
import com.podo.helloprice.api.global.rest.status.DefaultApiStatus;
import org.springframework.http.HttpStatus;

public class AlreadyNotifiedProductSaleApiException extends ApiException {

    private Long productSaleId;

    public AlreadyNotifiedProductSaleApiException(Long productSaleId) {
        super("이미 등록되어있는 상품 판매 알림입니다 : " + productSaleId);

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
