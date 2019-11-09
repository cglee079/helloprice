package com.podo.itemwatcher.core.domain.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemSaleStatus {

    SALE("판매중"),
    DISCONTINUE("단종"),
    EMPTY_ACCOUNT("일시품절"),
    NO_SUPPORT("가격비교중지"),
    ERROR("알 수 없음");

    private final String value;


}
