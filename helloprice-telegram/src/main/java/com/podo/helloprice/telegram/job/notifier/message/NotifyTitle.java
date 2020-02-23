package com.podo.helloprice.telegram.job.notifier.message;

import com.podo.helloprice.core.util.MyCurrencyUtils;
import com.podo.helloprice.telegram.domain.item.ItemDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NotifyTitle {

    public static String notifyItemDiscontinued(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("'")
                .append(itemDetail.getItemName())
                .append("' 상품은 '단종' 상태로 변경되었습니다")
                .toString();
    }

    public static String notifyItemUnknown(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("'")
                .append(itemDetail.getItemName())
                .append("' 상품은 '알 수 없는' 상태로 변경되었습니다")
                .toString();
    }

    public static String notifyItemEmptyAccount(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("'")
                .append(itemDetail.getItemName())
                .append("' 상품은 '재고없음' 상태로 변경되었습니다")
                .toString();
    }

    public static String notifyItemNotSupport(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("'")
                .append(itemDetail.getItemName())
                .append("' 상품은 '가격비교중지' 상태로 변경되었습니다")
                .toString();
    }


    public static String notifyItemSale(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("'")
                .append(itemDetail.getItemName())
                .append("' 상품의 최저가격이  ")
                .append(MyCurrencyUtils.toKrw(itemDetail.getItemPrice()))
                .append(" 으로 갱신되었습니다.")
                .toString();
    }

    public static String notifyItemReSale(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("'")
                .append(itemDetail.getItemName())
                .append("' 상품이 ")
                .append(MyCurrencyUtils.toKrw(itemDetail.getItemPrice()))
                .append(" 으로 다시 판매를 시작했습니다.")
                .toString();
    }

    public static String notifyItemDead(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("'")
                .append(itemDetail.getItemName())
                .append("' 상품은 더 이상 페이지를 확인 할 수 없습니다")
                .toString();
    }

    public static String notifyTooManyDead(int deadCount) {
        return new StringBuilder()
                .append("관리자님 너무 많은 상품페이지를 확인 할 수 없습니다. ")
                .append("DEAD COUNT : ")
                .append(deadCount)
                .toString();
    }

    public static String notifyTooManyUnknown(int deadCount) {
        return new StringBuilder()
                .append("관리자님 너무 많은 상품 상태를 확인 할 수 없습니다.\n")
                .append("DEAD COUNT : ")
                .append(deadCount)
                .toString();
    }


}
