package com.podo.helloprice.code.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductSaleStatus {

    SALE("판매중"),
    DISCONTINUE("단종"),
    EMPTY_AMOUNT("일시품절"),
    NOT_SUPPORT("가격비교중지"),
    UNKNOWN("알 수 없음");

    private final String value;


}