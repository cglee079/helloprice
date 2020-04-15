package com.podo.helloprice.telegram.app.menu.email.add;

import com.podo.helloprice.telegram.app.menu.product.ProductCommonResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EmailAddResponse {

    public static String explain() {
        return new StringBuilder()
                .append("<b>이메일을 입력해주세요!</b>\n")
                .append("\n")
                .append("이메일을 등록하면, 이메일로도 알림이 전송되요!")
                .toString();
    }

    public static String invalidEmail() {
        return new StringBuilder()
                .append("<b>이메일 형식이 아닙니다..</b>\n")
                .append("\n")
                .append(ProductCommonResponse.toHome())
                .toString();
    }

}
