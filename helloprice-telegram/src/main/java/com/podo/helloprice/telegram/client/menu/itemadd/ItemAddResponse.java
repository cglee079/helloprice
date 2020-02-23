package com.podo.helloprice.telegram.client.menu.itemadd;

import com.podo.helloprice.core.domain.item.vo.CrawledItem;
import com.podo.helloprice.telegram.client.menu.global.CommonResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ItemAddResponse {

    public static String explain(String url, String helpUrl) {
        return new StringBuilder()
                .append("다나와에서 상품페이지의 URL(링크)을 입력해주세요!\n")
                .append("\n")

                .append("다나와 : ")
                .append(url)
                .append("\n")
                .append(CommonResponse.help(helpUrl))


                .toString();

    }

    public static String wrongItemUrl(String url) {
        return new StringBuilder().append("죄송합니다, 상품페이지의 URL이 잘못되었습니다\n")
                .append("URL : ")
                .append(url)
                .append("\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }

    public static String wrongItemCode(String itemCode) {
        return new StringBuilder()
                .append("죄송합니다, 상품 정보를 가져 올 수 없습니다\n")

                .append("\n")
                .append("상품코드 : ")
                .append(itemCode)
                .append("\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }

    public static String successAddNotifyItem() {

        return new StringBuilder()
                .append("상품 가격알림이 등록되었습니다\n")
                .append("지금부터 상품의 <b>최저가격</b>이 갱신되면 알림이 전송됩니다!\n")
                .toString();

    }

    public static String alreadySetNotifyItem() {
        return new StringBuilder()
                .append("이미 알림이 등록된 상품입니다\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }


    public static String isDiscontinueItem(CrawledItem crawledItem) {
        return new StringBuilder()
                .append(CommonResponse.descCrawledItemVo(crawledItem))
                .append("\n")
                .append("죄송합니다, 페이지에서 단종된 상품입니다")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }

    public static String isErrorItem(CrawledItem crawledItem) {
        return new StringBuilder()
                .append(CommonResponse.descCrawledItemVo(crawledItem))
                .append("\n")
                .append("죄송합니다, 페이지에서 알 수 없는 상태의 상품입니다")
                .append("\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }

    public static String isNotSupportItem(CrawledItem crawledItem) {
        return new StringBuilder()
                .append(CommonResponse.descCrawledItemVo(crawledItem))
                .append("\n")
                .append("죄송합니다, 페이지에서 가격비교를 제공하지 않는 상품입니다")
                .append("\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }

    public static String hasMaxItem() {
        return new StringBuilder()
                .append("죄송합니다, 최대 상품 알림 수를 초과했습니다.")
                .append("\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }
}
