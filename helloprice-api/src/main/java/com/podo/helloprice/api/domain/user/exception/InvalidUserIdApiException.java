package com.podo.helloprice.api.domain.user.exception;

import com.podo.helloprice.api.global.rest.ApiException;
import com.podo.helloprice.api.global.rest.status.DefaultApiStatus;
import org.springframework.http.HttpStatus;

public class InvalidUserIdApiException extends ApiException {

    private Long userId;

    public InvalidUserIdApiException(Long userId) {
        super("찾을 수 없는 사용자 ID 입니다 : " + userId);
        this.userId = userId;
    }

    @Override
    public String getField() {
        return "userId";
    }

    @Override
    public Object getValue() {
        return userId;
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
