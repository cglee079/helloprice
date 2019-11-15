package com.podo.helloprice.telegram.client.menu.itemserachadd;

import com.podo.helloprice.telegram.client.menu.global.CommonResponse;

public class ItemSearchAddResponse {

    public static String explain() {
        return new StringBuilder()
                .append("<b>해당 상품이 맞으신가요?</b>\n")
                .append("\n")
                .append("알림을 추가하시겠습니까\n")
                .toString();

    }

    public static String cantPoolItem() {
        return new StringBuilder()
                .append("죄송합니다.. 상품 상세 정보를 가져올 수가 없습니다..\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }
}
