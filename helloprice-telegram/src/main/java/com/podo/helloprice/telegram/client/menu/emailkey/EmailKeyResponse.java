package com.podo.helloprice.telegram.client.menu.emailkey;

import com.podo.helloprice.telegram.client.menu.global.CommonResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EmailKeyResponse {

    public static String explain() {
        return new StringBuilder()
                .append("<b>이메일을 발송했어요!</b>\n")
                .append("<b>이메일로 보낸 KEY를 입력해주세요!</b>\n")
                .toString();
    }

    public static String invalidKey() {
        return new StringBuilder()
                .append("<b>잘못된 KEY 입니다...</b>\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }

    public static String success() {
        return new StringBuilder()
                .append("<b>이메일등록에 성공했습니다</b>\n")
                .append("이제부터 이메일로도 알림이 전송되요!\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }
}
