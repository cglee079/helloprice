package com.podo.helloprice.telegram.client.menu.itemsearch;

import com.podo.helloprice.telegram.client.menu.global.CommonResponse;

public class ItemSearchResponse {

    public static String explain() {
        return new StringBuilder()
                .append("<b>검색어를 입력해주세요!</b>\n")
                .append("\n")
                .append("출처 : 다나와")
                .toString();
    }

    public static String noResult() {
        return new StringBuilder()
                .append("검색결과가 없습니다\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }
}
