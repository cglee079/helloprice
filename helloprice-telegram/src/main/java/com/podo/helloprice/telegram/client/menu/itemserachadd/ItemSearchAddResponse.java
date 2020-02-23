package com.podo.helloprice.telegram.client.menu.itemserachadd;

import com.podo.helloprice.telegram.client.menu.global.CommonResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ItemSearchAddResponse {

    public static String explain() {
        return new StringBuilder()
                .append("<b>해당 상품의 알림을 추가할까요?</b>\n")
                .toString();
    }

    public static String failPoolItem() {
        return new StringBuilder()
                .append("죄송합니다.. 상품 상세 정보를 가져올 수가 없습니다..\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }
}
