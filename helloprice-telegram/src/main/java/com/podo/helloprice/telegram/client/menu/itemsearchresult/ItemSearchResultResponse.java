package com.podo.helloprice.telegram.client.menu.itemsearchresult;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ItemSearchResultResponse {

    public static String explain() {
        return new StringBuilder()
                .append("<b>알림을 추가하고 싶은 상품을 선택해주세요!</b>\n")
                .toString();

    }

}
