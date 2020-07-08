package com.podo.helloprice.telegram.domain.tuser.exception;

public class InvalidTUserIdException extends RuntimeException {

    public InvalidTUserIdException(Long id) {
        super("유효하지 않는 유저 ID 입니다. Id : " + id);
    }
}
