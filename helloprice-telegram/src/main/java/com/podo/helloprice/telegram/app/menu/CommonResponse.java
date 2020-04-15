package com.podo.helloprice.telegram.app.menu;

import com.podo.helloprice.code.model.ProductAliveStatus;
import com.podo.helloprice.code.model.ProductSaleStatus;
import com.podo.helloprice.core.util.CalculateUtil;
import com.podo.helloprice.core.util.DateTimeUtil;
import com.podo.helloprice.telegram.domain.product.dto.ProductDetailDto;
import lombok.experimental.UtilityClass;

import static com.podo.helloprice.core.util.CurrencyUtil.toKrw;

@UtilityClass
public class CommonResponse {

    public static final String DATE_TIME_FORMAT = "yyyy년 MM월 dd일 HH시 mm분";

    public static String introduce(String appDesc) {
        return new StringBuilder()
                .append("<b>")
                .append(appDesc)
                .append("</b>")
                .append("\n")
                .append("\n")

                .append("안녕하세요!\n")
                .append("최저가 알림을 주는 ")
                .append(appDesc)
                .append(" 입니다!\n")
                .append("\n")

                .append("상품을 등록하면\n")
                .append("최저가격이 갱신됬을때!\n")
                .append("재고가 생겼을 때! \n")
                .append("텔레그램을 통해 알람을 드려요!!! \n")
                .append("\n")

                .toString();
    }

    public static String help(String helpUrl) {
        return new StringBuilder()
                .append("도움말 : ")
                .append(helpUrl)
                .toString();
    }

    public static String wrongInput() {
        return new StringBuilder()
                .append("잘못된 값을 입력하셨어요...")
                .append("\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }


    public static String toHome() {
        return new StringBuilder()
                .append("<b>홈 메뉴로 돌아갑니다!</b>\n")
                .toString();
    }


    public static String justWait() {
        return "<b>잠시만 기다려주세요!</b>";
    }

    public static String seeKeyboardIcon() {
        return "<b>중요!!\n 오른쪽아래에 버튼 아이콘을 눌러주세요!!!\n 이쁜 버튼이 보여요!!</b>";
    }
}
