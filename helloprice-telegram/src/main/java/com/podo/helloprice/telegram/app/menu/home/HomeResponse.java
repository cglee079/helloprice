package com.podo.helloprice.telegram.app.menu.home;

import com.podo.helloprice.telegram.app.menu.product.ProductCommonResponse;
import com.podo.helloprice.telegram.domain.product.dto.ProductDetailDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HomeResponse {

    public static String descProduct(ProductDetailDto product) {
        return new StringBuilder()
                .append("현재 상품정보는 다음과 같습니다\n")
                .append("\n")
                .append(ProductCommonResponse.descProductDetailWithChangeMessage(product))
                .toString();
    }

    public static String wrongProductCode(String productCode) {
        return "상품코드가 잘못되었어요, 상품코드 : " + productCode;
    }

    public static String rejectEmailAdd(String email) {
        return new StringBuilder()
                .append("<b>이미 이메일이 등록되어있습니다</b>\n")
                .append("등록된 이메일 : ")
                .append(email)
                .append("\n")
                .append("\n")
                .append("이메일 삭제 후 다시 시도 해주세요")
                .toString();
    }

    public static String dontHaveEmail() {
        return new StringBuilder()
                .append("<b>등록되어있는 이메일이 없습니다.</b>\n")
                .toString();
    }
}
