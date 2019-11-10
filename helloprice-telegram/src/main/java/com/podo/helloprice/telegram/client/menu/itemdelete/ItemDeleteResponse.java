package com.podo.helloprice.telegram.client.menu.itemdelete;

import com.podo.helloprice.core.domain.item.ItemInfoVo;
import com.podo.helloprice.core.util.MyCurrencyUtils;
import com.podo.helloprice.core.util.MyFormatUtils;
import com.podo.helloprice.telegram.domain.item.ItemDto;

public class ItemDeleteResponse {

    public static String explain() {
        StringBuilder message = new StringBuilder();

        message.append("삭제할 상품을 선택해주세요\n")
                .append("삭제된 상품은 더 이상 알림이 가지않습니다");

        return message.toString();

    }

    public static String alreadyNotNotifyItem() {
        return new StringBuilder().append("알림이 등록되어있지 않은 상품입니다\n")
                .toString();
    }

    public static String deletedNotifyItem(ItemDto.detail itemDetail) {
        return new StringBuilder()

                .append("상품코드 : ")
                .append(itemDetail.getItemCode())
                .append("\n")

                .append("상품이름 : ")
                .append(itemDetail.getItemName())
                .append("\n")
                .append("\n")

                .append("알림이 삭제되었습니다\n")

                .toString();
    }

}
