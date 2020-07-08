package com.podo.helloprice.api.global.rest;

import com.podo.helloprice.api.global.rest.status.DefaultApiStatus;
import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException {

    public ApiException(String message) {
        super(message);
    }

    abstract public String getField();
    abstract public Object getValue();
    abstract public HttpStatus getHttpStatus();
    abstract public DefaultApiStatus getApiStatus();
}
