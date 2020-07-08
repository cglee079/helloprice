package com.podo.helloprice.core.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RequestHeader {

    USER_AGENT("User-Agent"),
    AUTHORIZATION("Authorization"),
    ACCESS_TOKEN("Access-Token"),
    X_REAL_IP("x-real-ip");

    private final String value;

    public String value(){
        return value;
    }
}
