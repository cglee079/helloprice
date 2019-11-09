package com.podo.itemwatcher.telegram.global.infra.telegram.message;

import com.podo.itemwatcher.core.util.MyCurrencyUtils;
import com.podo.itemwatcher.core.util.MyFormatUtils;
import com.podo.itemwatcher.telegram.domain.item.ItemDto;

public class MessageMaker {

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

    public static String explainAddItem() {
        StringBuilder message = new StringBuilder();

        message.append("다나와에서 아이템페이지의 URL을 입력해주세요! \n");

        return message.toString();

    }

    public static String explainDeleteItem() {
        StringBuilder message = new StringBuilder();

        message.append("삭제할 아이템을 선택해주세요\n")
                .append("삭제된 아이템은 더 이상 알림이 가지않습니다");

        return message.toString();

    }

    public static String wrongItemUrl(String url) {
        StringBuilder message = new StringBuilder();

        message.append("아이템 페이지의 URL이 잘못되었습니다\n")
                .append("URL : ")
                .append(url);

        return message.toString();
    }

    public static String successAddItem(ItemDto.detail itemDetail) {

        return new StringBuilder()
                .append("상품 가격알림이 등록되었습니다\n")
                .append("지금부터 상품의 최저가격이 갱신되면 알림이 전송됩니다!")
                .append("\n")
                .append("\n")

                .append(descItem(itemDetail))
                .toString();

    }

    public static String alreadySetNotifyItem(ItemDto.detail itemDetail) {
        StringBuilder message = new StringBuilder();

        message.append("이미 알림이 등록되어있습니다\n")
                .append(descItem(itemDetail));

        return message.toString();

    }

    public static String makeItemInfo(ItemDto.detail itemDetail) {

        return new StringBuilder()
                .append("현재 상품정보는 다음과 같습니다")
                .append("\n")
                .append("\n")
                .append(descItem(itemDetail))
                .toString();


    }


    public static String wrongItemCode(String itemCode) {
        return "상품코드가 잘못되었습니다, 상품코드 : " + itemCode;
    }

    private static String descItem(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("최종체크일 : ")
                .append(MyFormatUtils.dateTimeToString(itemDetail.getLastPoolAt(), "yyyy-MM-dd HH:mm:ss"))
                .append("\n")

                .append("<b>")
                .append("가격갱신일 : ")
                .append(MyFormatUtils.dateTimeToString(itemDetail.getLastUpdateAt(), "yyyy-MM-dd HH:mm:ss"))
                .append("<b>")
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
}
