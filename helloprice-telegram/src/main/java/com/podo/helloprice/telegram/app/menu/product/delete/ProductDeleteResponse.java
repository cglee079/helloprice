package com.podo.helloprice.telegram.app.menu.product.delete;

import com.podo.helloprice.telegram.app.menu.CommonResponse;
import com.podo.helloprice.telegram.domain.product.dto.ProductOnePriceTypeDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductDeleteResponse {

    public static String explain() {
        StringBuilder message = new StringBuilder();

        message.append("삭제할 상품을 선택해주세요\n")
                .append("삭제된 상품은 더 이상 알림이 가지않습니다");

        return message.toString();

    }

    public static String alreadyNotNotifyProduct() {
        return new StringBuilder()
                .append("알림이 등록되어있지 않은 상품입니다\n")
                .append("\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }

    public static String deletedNotifyProduct(ProductOnePriceTypeDto product) {
        return new StringBuilder()

                .append("상품코드 : ")
                .append(product.getProductCode())
                .append("\n")

                .append("상품이름 : ")
                .append(product.getProductName())
                .append("\n")
                .append("\n")

                .append("알림이 삭제되었습니다\n")

                .toString();
    }

}
