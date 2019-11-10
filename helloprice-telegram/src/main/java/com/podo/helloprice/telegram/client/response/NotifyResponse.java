package com.podo.helloprice.telegram.client.response;

import com.podo.helloprice.telegram.domain.item.ItemDto;

public class NotifyResponse {


    public static String notifyItemDiscontinued(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품은 '단종' 상태로 변경되었습니다\n")
                .append("더 이상 해당 상품은 알림이 전송되지 않습니다\n")
                .append("</b>")
                .append("\n")
                .append(CommonResponse.descItemDetail(itemDetail))
                .append("\n")
                .toString();
    }

    public static String notifyItemError(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품은 '알 수 없는' 상태로 변경되었습니다\n")
                .append("더 이상 해당 상품은 알림이 전송되지 않습니다\n")
                .append("</b>")
                .append("\n")
                .append(CommonResponse.descItemDetail(itemDetail))
                .append("\n")
                .toString();
    }

    public static String notifyItemEmptyAccount(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품은 '재고없음' 상태로 변경되었습니다\n")
                .append("</b>")
                .append("\n")
                .append(CommonResponse.descItemDetail(itemDetail))
                .append("\n")
                .toString();
    }

    public static String notifyItemSale(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품은 최저가가 갱신되었습니다!!\n")
                .append("</b>")
                .append("\n")
                .append(CommonResponse.descItemDetail(itemDetail))
                .append("\n")
                .toString();
    }

    public static String notifyItemDead(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품의 상품페이지를 더이상 확인 할 수 없습니다.\n")
                .append("더 이상 해당 상품은 알림이 전송되지 않습니다\n")
                .append("</b>")
                .append("\n")
                .append(CommonResponse.descItemDetail(itemDetail))
                .append("\n")
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

    public static String notifyTooManyError(int deadCount) {
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
