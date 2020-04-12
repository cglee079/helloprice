package com.podo.helloprice.telegram.app.menu.productadd;

import com.podo.helloprice.crawl.worker.vo.CrawledProduct;
import com.podo.helloprice.telegram.app.menu.global.CommonResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductAddResponse {

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

    public static String wrongProductUrl(String url) {
        return new StringBuilder().append("죄송합니다, 상품페이지의 URL이 잘못되었습니다\n")
                .append("URL : ")
                .append(url)
                .append("\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }

    public static String wrongProductCode(String productCode) {
        return new StringBuilder()
                .append("죄송합니다, 상품 정보를 가져 올 수 없습니다\n")

                .append("\n")
                .append("상품코드 : ")
                .append(productCode)
                .append("\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }

    public static String successAddNotifyProduct() {

        return new StringBuilder()
                .append("상품 가격알림이 등록되었습니다\n")
                .append("지금부터 상품의 <b>최저가격</b>이 갱신되면 알림이 전송됩니다!\n")
                .toString();

    }

    public static String alreadySetNotifyProduct() {
        return new StringBuilder()
                .append("이미 알림이 등록된 상품입니다\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }


    public static String isDiscontinueProduct(CrawledProduct crawledProduct) {
        return new StringBuilder()
                .append(CommonResponse.descCrawledProductVo(crawledProduct))
                .append("\n")
                .append("죄송합니다, 페이지에서 단종된 상품입니다")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }

    public static String isErrorProduct(CrawledProduct crawledProduct) {
        return new StringBuilder()
                .append(CommonResponse.descCrawledProductVo(crawledProduct))
                .append("\n")
                .append("죄송합니다, 페이지에서 알 수 없는 상태의 상품입니다")
                .append("\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }

    public static String isNotSupportProduct(CrawledProduct crawledProduct) {
        return new StringBuilder()
                .append(CommonResponse.descCrawledProductVo(crawledProduct))
                .append("\n")
                .append("죄송합니다, 페이지에서 가격비교를 제공하지 않는 상품입니다")
                .append("\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }

    public static String hasMaxProduct() {
        return new StringBuilder()
                .append("죄송합니다, 최대 상품 알림 수를 초과했습니다.")
                .append("\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }
}
