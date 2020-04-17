package com.podo.helloprice.telegram.app.menu.product.search;

import com.podo.helloprice.telegram.app.menu.CommonResponse;
import com.podo.helloprice.telegram.app.menu.product.ProductCommonResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductSearchResponse {

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
