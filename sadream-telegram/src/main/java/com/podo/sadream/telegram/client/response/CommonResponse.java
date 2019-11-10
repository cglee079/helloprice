package com.podo.sadream.telegram.client.response;

import com.podo.sadream.core.domain.item.ItemInfoVo;
import com.podo.sadream.core.util.MyCurrencyUtils;
import com.podo.sadream.core.util.MyFormatUtils;
import com.podo.sadream.telegram.domain.item.ItemDto;

public class CommonResponse {

    public static String introduce(String username) {
        StringBuilder message = new StringBuilder();

        message.append(username)
                .append("님 안녕하세요! \n")
                .append("본 서비스는 최저가 알림을 주는 봇입니다! \n")
                .append("아이템을 등록하면, 최저가 갱신됬을때, \n")
                .append("텔레그램을 통해 알람을 드려요! \n");

        return message.toString();
    }

    public static String wrongInput() {
        return "잘못된 값을 입력하셨습니다";
    }

    public static String descItemDetail(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("<b>")
                .append("최종체크일 : ")
                .append(MyFormatUtils.dateTimeToString(itemDetail.getLastPoolAt(), "yyyy-MM-dd HH:mm:ss"))
                .append("</b>")
                .append("\n")

                .append("가격변동일 : ")
                .append(MyFormatUtils.dateTimeToString(itemDetail.getLastUpdateAt(), "yyyy-MM-dd HH:mm:ss"))
                .append("\n")
                .append("\n")

                .append("상품링크 : ")
                .append(itemDetail.getItemUrl())
                .append("\n")

                .append("상품코드 : ")
                .append(itemDetail.getItemCode())
                .append("\n")

                .append("상품이름 : ")
                .append(itemDetail.getItemName())
                .append("\n")

                .append("상품상태 : ")
                .append(itemDetail.getItemSaleStatus().getValue())
                .append("\n")

                .append("이전가격 : ")
                .append(MyCurrencyUtils.toExchangeRateKRWStr(itemDetail.getItemBeforePrice()))
                .append("\n")

                .append("<b>")
                .append("현재가격 : ")
                .append(MyCurrencyUtils.toExchangeRateKRWStr(itemDetail.getItemPrice()))
                .append("</b>")
                .append("\n")

                .toString();

    }

    public static String descItemInfoVo(ItemInfoVo itemInfoVo) {
        return new StringBuilder()
                .append("상품코드 : ")
                .append(itemInfoVo.getItemCode())
                .append("\n")

                .append("상품이름 : ")
                .append(itemInfoVo.getItemName())
                .append("\n")
                .toString();
    }

    public static String exit() {
        return new StringBuilder()
                .append("홈메뉴로 돌아갑니다\n")
                .toString();
    }

}
