package com.podo.helloprice.telegram.domain.user.exception;

public class InvalidTelegramIdException extends RuntimeException {
    public InvalidTelegramIdException(String telegramId) {
        super("유효하지 않는 텔레그램 ID 입니다. 텔레그램ID : " + telegramId);
    }
}
