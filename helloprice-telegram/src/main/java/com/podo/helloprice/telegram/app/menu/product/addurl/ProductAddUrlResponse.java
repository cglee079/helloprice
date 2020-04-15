package com.podo.helloprice.telegram.app.menu.product.addurl;

import com.podo.helloprice.telegram.app.menu.product.ProductCommonResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductAddUrlResponse {

    public static String explain(String url, String helpUrl) {
        return new StringBuilder()
                .append("다나와에서 상품페이지의 URL(링크)을 입력해주세요!\n")
                .append("\n")

                .append("다나와 : ")
                .append(url)
                .append("\n")
                .append(ProductCommonResponse.help(helpUrl))

                .toString();

    }

    public static String wrongProductUrl(String url) {
        return new StringBuilder().append("죄송합니다, 상품페이지의 URL이 잘못되었습니다\n")
                .append("URL : ")
                .append(url)
                .append("\n")
                .append("\n")
                .append(ProductCommonResponse.toHome())
                .toString();
    }

}
