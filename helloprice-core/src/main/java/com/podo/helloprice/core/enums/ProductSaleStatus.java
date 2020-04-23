package com.podo.helloprice.core.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProductSaleStatus {

    SALE("판매중"),
    DISCONTINUE("단종"),
    EMPTY_AMOUNT("일시품절"),
    NOT_SUPPORT("가격비교중지"),
    UNKNOWN("알 수 없음");

    private final String kr;

    public String kr(){
        return this.kr;
    }


}
