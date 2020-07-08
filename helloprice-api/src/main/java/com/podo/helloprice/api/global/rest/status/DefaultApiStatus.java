package com.podo.helloprice.api.global.rest.status;

import com.podo.helloprice.api.global.rest.ApiStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DefaultApiStatus implements ApiStatus {

    ERR_INVALID("400", "유효하지 않은 값입니다"),
    ERR_ID_DUPLICATE("401", "중복되는 값입니다"),

    ERR_SERVER_ERROR("500", "서버 에러"),
    ERR_STORAGE_SERVER("510", "Storage 서버 에러"),
    ERR_SOLR_SERVER("520", "검색 서버 에러"),
    ERR_TELEGRAM_SERVER("512", "텔레그램 서버 에러"),
    ERR_FILE_PROCESS("513", "파일 처리 에러"),
    UNAUTHORIZED("403", "권한 없음");

    private final String code;
    private final String message;


    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
