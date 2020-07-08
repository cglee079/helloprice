package com.podo.helloprice.api.global.security.oauth.exception;

public class InvalidOAuthTypeException extends RuntimeException {

    public InvalidOAuthTypeException(String id) {
        super("알 수 없는 OAuth ID 입니다 : " + id);

    }
}
