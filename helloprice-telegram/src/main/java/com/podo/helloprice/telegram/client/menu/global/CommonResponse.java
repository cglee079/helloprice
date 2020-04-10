package com.podo.helloprice.telegram.client.menu.global;

import com.podo.helloprice.core.domain.item.model.ItemSaleStatus;
import com.podo.helloprice.core.domain.item.model.ItemStatus;
import com.podo.helloprice.core.domain.item.vo.CrawledItem;
import com.podo.helloprice.core.util.CalculateUtil;
import com.podo.helloprice.core.util.CurrencyUtil;
import com.podo.helloprice.core.util.DateTimeUtil;
import com.podo.helloprice.telegram.domain.item.ItemDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonResponse {

    public static final String DATE_TIME_FORMAT = "yyyy년 MM월 dd일 HH시 mm분";

    public static String introduce(String appDesc) {
        return new StringBuilder()
                .append("<b>")
                .append(appDesc)
                .append("</b>")
                .append("\n")
                .append("\n")

                .append("안녕하세요!\n")
                .append("최저가 알림을 주는 ")
                .append(appDesc)
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
                .append("도움말 : ")
                .append(helpUrl)
                .toString();
    }

    public static String wrongInput() {
        return new StringBuilder()
                .append("잘못된 값을 입력하셨어요...")
                .append("\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }

    public static String descItemDetail(ItemDto.detail itemDetail) {

        StringBuilder message = new StringBuilder();
        Integer itemPrice = itemDetail.getItemPrice();
        Integer itemBeforePrice = itemDetail.getItemBeforePrice();

        message.append("<b>")
                .append("최종확인시간 : ")
                .append(DateTimeUtil.dateTimeToString(itemDetail.getLastPoolAt(), DATE_TIME_FORMAT))
                .append("</b>")
                .append("\n")

                .append("<b>가격변동시간</b> : ")
                .append(DateTimeUtil.dateTimeToString(itemDetail.getLastUpdateAt(), DATE_TIME_FORMAT))
                .append("\n")
                .append("\n")

                .append("<b>상품링크</b> : ")
                .append(itemDetail.getItemUrl())
                .append("\n")

                .append("<b>상품코드</b> : ")
                .append(itemDetail.getItemCode())
                .append("\n")

                .append("<b>")
                .append("상품이름 : ")
                .append(itemDetail.getItemName())
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("상품상태 : ")
                .append(itemDetail.getItemSaleStatus().getValue())
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("이전가격 : ")
                .append(CurrencyUtil.toKrw(itemBeforePrice))
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("현재가격 : ")
                .append(CurrencyUtil.toKrw(itemPrice))
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("가격변화 : ")
                .append(CalculateUtil.getPercentStringWithPlusMinusSign(itemPrice, itemBeforePrice))
                .append("</b>");


        return message.toString();
    }

    public static String descItemDetailWithChangeMessage(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append(descItemDetail(itemDetail))
                .append("\n")
                .append("\n")
                .append(descItemChange(itemDetail))
                .toString();
    }

    public static String descItemChange(ItemDto.detail itemDetail) {
        if (itemDetail.getItemStatus().equals(ItemStatus.DEAD)) {
            return "죄송합니다.. <b>상품의 페이지를 확인 할 수 없어요..</b>";
        }

        return descChangeBySaleStatus(itemDetail);
    }

    private static String descChangeBySaleStatus(ItemDto.detail itemDetail) {

        final ItemSaleStatus itemSaleStatus = itemDetail.getItemSaleStatus();
        switch (itemSaleStatus) {
            case UNKNOWN:
                return "죄송합니다.. <b>상품의 상태를 알 수 없어요..</b>";
            case DISCONTINUE:
                return "죄송합니다.. <b>상품이 단종 됬어요..</b>";
            case NOT_SUPPORT:
                return "죄송합니다.. <b>상품의 가격비교가 중지되었어요..</b>";
            case EMPTY_AMOUNT:
                return "죄송합니다.. <b>상품의 재고가 없어요..</b>";
            case SALE:
                return descSaleStatusChange(itemDetail.getItemPrice(), itemDetail.getItemBeforePrice());
        }

        return "";
    }

    private static String descSaleStatusChange(Integer itemPrice, Integer itemBeforePrice) {
        final StringBuilder message = new StringBuilder();
        if (itemBeforePrice.equals(0)) {
            message.append("야호!  <b>")
                    .append(CurrencyUtil.toKrw(itemPrice))
                    .append("</b>으로 다시 판매를 시작했어요!!");
            return message.toString();
        }

        if (itemPrice > itemBeforePrice) {
            message.append("죄송합니다.. 가격이 <b>")
                    .append(CurrencyUtil.toKrw(itemPrice - itemBeforePrice))
                    .append("</b> 올랐어요...");
            return message.toString();
        }

        if (itemPrice.equals(itemBeforePrice)) {
            message.append("<i>아직 가격이 똑같아요! 좀만 더 기다려보세요!</i>");
            return message.toString();
        }

        message.append("야호! 가격이 <b>")
                .append(CurrencyUtil.toKrw(itemBeforePrice - itemPrice))
                .append("</b> 떨어졌어요!!");

        return message.toString();
    }


    public static String descCrawledItemVo(CrawledItem crawledItem) {
        return new StringBuilder()
                .append("<b>상품코드</b> : ")
                .append(crawledItem.getItemCode())
                .append("\n")

                .append("<b>")
                .append("상품이름 : ")
                .append(crawledItem.getItemName())
                .append("</b>")
                .append("\n")

                .append("<b>상품설명</b> : ")
                .append("<i>")
                .append(crawledItem.getItemDesc())
                .append("</i>")
                .append("\n")

                .append("<b>")
                .append("상품상태 : ")
                .append(crawledItem.getItemSaleStatus().getValue())
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("상품가격 : ")
                .append(CurrencyUtil.toKrw(crawledItem.getItemPrice()))
                .append("</b>")
                .append("\n")

                .toString();
    }


    public static String toHome() {
        return new StringBuilder()
                .append("<b>홈 메뉴로 돌아갑니다!</b>\n")
                .toString();
    }


    public static String justWait() {
        return "<b>잠시만 기다려주세요!</b>";
    }

    public static String seeKeyboardIcon() {
        return "<b>중요!!\n 오른쪽아래에 버튼 아이콘을 눌러주세요!!!\n 이쁜 버튼이 보여요!!</b>";
    }
}
