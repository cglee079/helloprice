package com.podo.helloprice.telegram.app.menu.emaildelete;

import com.podo.helloprice.telegram.app.menu.global.CommonResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EmailDeleteResponse {

    public static String explain(String email) {
        return new StringBuilder()
                .append("<b>정말 이메일을 삭제하시겠습니까?</b>\n")
                .append("삭제 후, 이메일에 더이상 알림이 전송되지 않습니다.\n")
                .append("\n")
                .append("등록된 이메일 : ")
                .append(email)
                .toString();
    }

    public static String success() {
        return new StringBuilder()
                .append("<b>이메일이 삭제되었습니다</b>\n")
                .append("이메일에 더이상 알림이 전송되지 않습니다.\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }
}
