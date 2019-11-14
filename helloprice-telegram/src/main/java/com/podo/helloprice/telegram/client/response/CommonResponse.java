package com.podo.helloprice.telegram.client.response;

import com.podo.helloprice.core.domain.item.ItemInfoVo;
import com.podo.helloprice.core.util.MyCurrencyUtils;
import com.podo.helloprice.core.util.MyFormatUtils;
import com.podo.helloprice.telegram.domain.item.ItemDto;
import com.podo.helloprice.core.util.MyCalculateUtils;

import java.text.DecimalFormat;

public class CommonResponse {
    public static final String DATE_TIME_FORMAT = "yyyy년 MM월 dd일 HH시 mm분";

    public static String introduce(String appName) {
        return new StringBuilder()
                .append("<b>")
                .append(appName)
                .append("</b>")
                .append("\n")
                .append("\n")

                .append("안녕하세요!\n")
                .append("최저가 알림을 주는 ")
                .append(appName)
                .append(" 입니다!\n")
                .append("\n")

                .append("상품을 등록하면\n")
                .append("최저가격이 갱신됬을때!\n")
                .append("재고가 생겼을 때! \n")
                .append("텔레그램을 통해 알람을 드려요!!! \n")
                .append("\n")

                .toString();
    }

    public static String help(String helpUrl) {
        return new StringBuilder()
                .append("도움말(사용법) : ")
                .append(helpUrl)
                .append("\n")
                .append("건의/버그제보는 해당 링크에 댓글로 부탁드립니다!")
                .toString();
    }

    public static String wrongInput() {
        return new StringBuilder()
                .append("잘못된 값을 입력하셨어요...")
                .append("\n")
                .append("\n")
                .append("홈으로 돌아갑니다!")
                .toString();
    }

    public static String descItemDetail(ItemDto.detail itemDetail) {

        StringBuilder message = new StringBuilder();
        Integer itemPrice = itemDetail.getItemPrice();
        Integer itemBeforePrice = itemDetail.getItemBeforePrice();

        message.append("")
                .append("<b>")
                .append("최종확인시간 : ")
                .append(MyFormatUtils.dateTimeToString(itemDetail.getLastPoolAt(), DATE_TIME_FORMAT))
                .append("</b>")
                .append("\n")

                .append("가격변동시간 : ")
                .append(MyFormatUtils.dateTimeToString(itemDetail.getLastUpdateAt(), DATE_TIME_FORMAT))
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

                .append("<b>")
                .append("상품상태 : ")
                .append(itemDetail.getItemSaleStatus().getValue())
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("이전가격 : ")
                .append(MyCurrencyUtils.toExchangeRateKRWStr(itemBeforePrice))
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("현재가격 : ")
                .append(MyCurrencyUtils.toExchangeRateKRWStr(itemPrice))
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("가격변화 : ")
                .append(CommonResponse.toSignPercentStr(itemPrice, itemBeforePrice))
                .append("</b>");

        return message.toString();
    }

    public static String descItemChange(ItemDto.detail itemDetail) {
        StringBuilder message = new StringBuilder();

        Integer itemPrice = itemDetail.getItemPrice();
        Integer itemBeforePrice = itemDetail.getItemBeforePrice();

        if (itemPrice > itemBeforePrice) {
            message.append("죄송합니다.. 가격이 <b>")
                    .append(MyCurrencyUtils.toExchangeRateKRWStr(itemPrice - itemBeforePrice))
                    .append("</b> 올랐어요...");
        } else if (itemPrice.equals(itemBeforePrice)) {
            message.append("아직 가격이 똑같아요! 좀만 더 기다려보세요!");
        } else {
            message.append("야호! 가격이 <b>")
                    .append(MyCurrencyUtils.toExchangeRateKRWStr(itemBeforePrice - itemPrice))
                    .append("</b> 떨어졌어요!!");
        }

        return message.toString();
    }


    public static String descItemInfoVo(ItemInfoVo itemInfoVo) {
        return new StringBuilder()
                .append("상품코드 : ")
                .append(itemInfoVo.getItemCode())
                .append("\n")

                .append("상품이름 : ")
                .append(itemInfoVo.getItemName())
                .append("\n")

                .append("<b>")
                .append("상품상태 : ")
                .append(itemInfoVo.getItemSaleStatus().getValue())
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("상품가격 : ")
                .append(MyCurrencyUtils.toExchangeRateKRWStr(itemInfoVo.getItemPrice()))
                .append("</b>")
                .append("\n")

                .toString();
    }


    public static String toHome() {
        return new StringBuilder()
                .append("<b>홈 메뉴로 돌아갑니다!!!!</b>\n")
                .toString();
    }


    public static String toSignPercentStr(double a, double b) {
        String prefix = "";

        double percent = MyCalculateUtils.getChangePercent(a, b);

        if (percent > 0) {
            prefix = "+";
        }

        DecimalFormat df = new DecimalFormat("#.##");
        return prefix + df.format(percent) + "%";
    }


    public static String justWait() {
        return "잠시만, 잠시만 기다려주세요!";
    }
}
