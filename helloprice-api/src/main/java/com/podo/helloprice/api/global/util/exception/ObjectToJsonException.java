package com.podo.helloprice.api.global.util.exception;

public class ObjectToJsonException extends RuntimeException {
    public ObjectToJsonException(Exception e, Class<?> clazz) {
        super("Object to JSON 형변환을 실패했습니다 " + clazz.getName(), e);
    }
}
