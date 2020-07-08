package com.podo.helloprice.api.global.rest.response.dto;

import lombok.Getter;

@Getter
public class ErrorDto {

    private String field;
    private Object value;
    private String message;

    public ErrorDto(String field, Object value, String message) {
        this.field = field;
        this.value = value;
        this.message = message;
    }
}
