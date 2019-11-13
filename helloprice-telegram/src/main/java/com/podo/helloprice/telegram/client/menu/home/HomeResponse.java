package com.podo.helloprice.telegram.client.menu.home;

import com.podo.helloprice.telegram.domain.item.ItemDto;
import com.podo.helloprice.telegram.client.response.CommonResponse;

public class HomeResponse {

    public static String itemDetail(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("현재 상품정보는 다음과 같습니다\n")
                .append("\n")
                .append(CommonResponse.descItemDetail(itemDetail))
                .append("\n")
                .append("\n")
                .append(CommonResponse.descItemChange(itemDetail))
                .toString();
    }

    public static String wrongItemCode(String itemCode) {
        return "상품코드가 잘못되었어요, 상품코드 : " + itemCode;
    }

}
