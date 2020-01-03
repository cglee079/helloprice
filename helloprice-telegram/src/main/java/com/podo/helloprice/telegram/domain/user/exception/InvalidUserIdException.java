package com.podo.helloprice.telegram.domain.user.exception;

public class InvalidUserIdException extends RuntimeException {

    public InvalidUserIdException(Long id) {
        super("유효하지 않는 유저 ID 입니다. Id : " + id);
    }
}
