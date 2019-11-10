package com.podo.helloprice.telegram.client.response;

import com.podo.helloprice.core.domain.item.ItemInfoVo;
import com.podo.helloprice.core.util.MyCurrencyUtils;
import com.podo.helloprice.core.util.MyFormatUtils;
import com.podo.helloprice.telegram.domain.item.ItemDto;

public class CommonResponse {

    public static String introduce(String appName, String helpUrl) {
        return new StringBuilder()
                .append("<b>")
                .append(appName)
                .append("</b>")
                .append("\n")
                .append("\n")

                .append("안녕하세요! \n ")
                .append("최저가 알림을 주는 ")
                .append(appName)
                .append("입니다!\n")
                .append("\n")

                .append("상품을 등록하면, 최저가 갱신됬을때, 재고가 생겼을때 \n")
                .append("텔레그램을 통해 알람을 드려요!!! \n")
                .append("\n")

                .append("자세한 내용은, 도움말 페이지를 참조해주세요\n")
                .append(CommonResponse.help(helpUrl))

                .toString();
    }

    public static String help(String helpUrl) {
        return new StringBuilder()
                .append("도움말 : ")
                .append(helpUrl)
                .append("\n")
                .append("제보는 해당 링크에 댓글로 부탁드립니다!")
                .toString();
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
