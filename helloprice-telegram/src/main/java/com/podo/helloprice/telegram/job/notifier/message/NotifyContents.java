package com.podo.helloprice.telegram.job.notifier.message;

import com.podo.helloprice.core.util.MyCalculateUtils;
import com.podo.helloprice.core.util.MyCurrencyUtils;
import com.podo.helloprice.telegram.client.menu.global.CommonResponse;
import com.podo.helloprice.telegram.domain.item.ItemDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NotifyContents {


    public static String notifyItemDiscontinued(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품은 '단종' 상태로 변경되었습니다\n")
                .append("더 이상 해당 상품은 알림이 전송되지 않습니다\n")
                .append("</b>")
                .append("\n")
                .append(CommonResponse.descItemDetailWithChangeMessage(itemDetail))
                .toString();
    }

    public static String notifyItemUnknown(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품은 '알 수 없는' 상태로 변경되었습니다\n")
                .append("더 이상 해당 상품은 알림이 전송되지 않습니다\n")
                .append("</b>")
                .append("\n")
                .append(CommonResponse.descItemDetailWithChangeMessage(itemDetail))
                .toString();
    }

    public static String notifyItemEmptyAccount(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품은 '재고없음' 상태로 변경되었습니다\n")
                .append("</b>")
                .append("\n")
                .append(CommonResponse.descItemDetailWithChangeMessage(itemDetail))
                .toString();
    }

    public static String notifyItemNotSupport(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품은 '가격비교중비' 상태로 변경되었습니다\n")
                .append("</b>")
                .append("\n")
                .append(CommonResponse.descItemDetailWithChangeMessage(itemDetail))
                .toString();
    }


    public static String notifyItemSale(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append(descSummary(itemDetail))
                .append("\n")
                .append("---------------------------------------\n")
                .append("\n")
                .append("<b>")
                .append("해당 상품은 최저가가 갱신되었습니다!!\n")
                .append("</b>")
                .append("\n")
                .append(CommonResponse.descItemDetailWithChangeMessage(itemDetail))
                .toString();
    }

    private static String descSummary(ItemDto.detail itemDetail) {
        final Integer itemPrice = itemDetail.getItemPrice();
        final Integer itemBeforePrice = itemDetail.getItemBeforePrice();

        return new StringBuilder()
                .append("<b>")
                .append("상품이름 : ")
                .append(itemDetail.getItemName())
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("현재가격 : ")
                .append(MyCurrencyUtils.toKrw(itemPrice))
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("가격변화 : ")
                .append(MyCalculateUtils.getPercentStringWithPlusMinusSign(itemPrice, itemBeforePrice))
                .append("</b>")
                .toString();
    }

    public static String notifyItemResale(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품은 다시 판매를 시작했습니다!\n")
                .append("</b>")
                .append("\n")
                .append(CommonResponse.descItemDetailWithChangeMessage(itemDetail))
                .toString();
    }

    public static String notifyItemDead(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품의 상품페이지를 더이상 확인 할 수 없습니다.\n")
                .append("더 이상 해당 상품은 알림이 전송되지 않습니다\n")
                .append("</b>")
                .append("\n")
                .append(CommonResponse.descItemDetailWithChangeMessage(itemDetail))
                .toString();
    }

    public static String notifyTooManyDead(int deadCount) {
        return new StringBuilder()
                .append("<b>")
                .append("관리자님 너무 많은 상품페이지를 확인 할 수 없습니다.\n")
                .append("</b>")
                .append("\n")
                .append("DEAD COUNT : ")
                .append(deadCount)
                .toString();
    }

    public static String notifyTooManyUnknown(int deadCount) {
        return new StringBuilder()
                .append("<b>")
                .append("관리자님 너무 많은 상품 상태를 확인 할 수 없습니다.\n")
                .append("</b>")
                .append("\n")
                .append("DEAD COUNT : ")
                .append(deadCount)
                .toString();
    }


}
