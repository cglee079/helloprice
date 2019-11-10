package com.podo.sadream.telegram.client.menu.itemadd;

import com.podo.itemwatcher.core.domain.item.ItemInfoVo;
import com.podo.sadream.telegram.client.CommonResponse;
import com.podo.sadream.telegram.domain.item.ItemDto;

public class ItemAddResponse {

    public static String explain(String url) {
        return new StringBuilder()
                .append("다나와에서 페이지의 URL(링크)을 입력해주세요!\n")
                .append("\n")

                .append("다나와 : ")
                .append(url)

                .toString();

    }

    public static String wrongItemUrl(String url) {
        StringBuilder message = new StringBuilder();

        message.append("상품페이지의 URL이 잘못되었습니다\n")
                .append("URL : ")
                .append(url);

        return message.toString();
    }

    public static String wrongItem(String url) {
        StringBuilder message = new StringBuilder();

        message.append("상품 정보를 가져 올 수 없습니다\n")
                .append("URL : ")
                .append(url);

        return message.toString();
    }

    public static String successAddNotifyItem(ItemDto.detail itemDetail) {

        return new StringBuilder()
                .append("상품 가격알림이 등록되었습니다\n")
                .append("지금부터 상품의 최저가격이 갱신되면 알림이 전송됩니다!\n")
                .append("\n")

                .append(CommonResponse.descItemDetail(itemDetail))
                .toString();

    }



    public static String alreadySetNotifyItem(ItemDto.detail itemDetail) {
        return new StringBuilder().append("이미 알림이 등록되어있습니다\n")
                .append("\n")
                .append(CommonResponse.descItemDetail(itemDetail))
                .toString();
    }

    public static String makeItemInfo(ItemDto.detail itemDetail) {
        return new StringBuilder()
                .append("현재 상품정보는 다음과 같습니다\n")
                .append("\n")
                .append(CommonResponse.descItemDetail(itemDetail))
                .toString();
    }


    public static String isDiscontinueItem(ItemInfoVo itemInfoVo) {
        return new StringBuilder()
                .append(CommonResponse.descItemInfoVo(itemInfoVo))
                .append("\n")
                .append("단종된 상품입니다")
                .toString();
    }

    public static String isErrorItem(ItemInfoVo itemInfoVo) {
        return new StringBuilder()
                .append(CommonResponse.descItemInfoVo(itemInfoVo))
                .append("\n")
                .append("알 수 없는 상태의 상품입니다")
                .toString();
    }

    public static String isNotSupportItem(ItemInfoVo itemInfoVo) {
        return new StringBuilder()
                .append(CommonResponse.descItemInfoVo(itemInfoVo))
                .append("\n")
                .append("가격비교를 제공하지 않는 상품입니다")
                .toString();
    }

}
