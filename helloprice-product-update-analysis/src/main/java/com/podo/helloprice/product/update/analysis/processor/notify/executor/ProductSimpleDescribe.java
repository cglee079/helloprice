package com.podo.helloprice.product.update.analysis.processor.notify.executor;

import com.podo.helloprice.core.util.DateTimeUtil;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductSimpleDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductSimpleDescribe {

    private static final String DATE_TIME_FORMAT = "yyyy년 MM월 dd일 HH시 mm분";

    public static String descProductSimple(ProductSimpleDto product) {

        return new StringBuilder().append("<b>")
                .append("최종확인시간 : ")
                .append(DateTimeUtil.dateTimeToString(product.getLastCrawledAt(), DATE_TIME_FORMAT))
                .append("</b>")
                .append("\n")

                .append("<b>상품코드</b> : ")
                .append(product.getProductCode())
                .append("\n")

                .append("<b>")
                .append("상품이름 : ")
                .append(product.getProductName())
                .append("</b>")
                .append("\n")

                .append("<b>상품링크</b> : ")
                .append(product.getUrl())
                .append("\n")

                .toString();


    }

}
