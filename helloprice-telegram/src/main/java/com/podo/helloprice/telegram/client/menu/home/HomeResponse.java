package com.podo.helloprice.telegram.client.menu.home;

import com.podo.helloprice.telegram.domain.item.ItemDto;
import com.podo.helloprice.telegram.client.menu.global.CommonResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HomeResponse {

    public static String itemDetail(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("현재 상품정보는 다음과 같습니다\n")
                .append("\n")
                .append(CommonResponse.descItemDetailWithChangeMessage(itemDetail))
                .toString();
    }

    public static String wrongItemCode(String itemCode) {
        return "상품코드가 잘못되었어요, 상품코드 : " + itemCode;
    }

    public static String rejectEmailAdd(String email) {
        return new StringBuilder()
                .append("<b>이미 이메일이 등록되어있습니다</b>\n")
                .append("등록된 이메일 : ")
                .append(email)
                .append("\n")
                .append("\n")
                .append("이메일 삭제 후 다시 시도 해주세요")
                .toString();
    }

    public static String dontHaveEmail() {
        return new StringBuilder()
                .append("<b>등록되어있는 이메일이 없습니다.</b>\n")
                .toString();
    }
}
