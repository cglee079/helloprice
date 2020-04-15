package com.podo.helloprice.telegram.app.menu.product;

import com.podo.helloprice.code.model.ProductAliveStatus;
import com.podo.helloprice.code.model.ProductSaleStatus;
import com.podo.helloprice.core.util.CalculateUtil;
import com.podo.helloprice.core.util.DateTimeUtil;
import com.podo.helloprice.telegram.domain.product.dto.ProductDetailDto;
import com.podo.helloprice.telegram.domain.product.model.PriceType;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import static com.podo.helloprice.core.util.CurrencyUtil.toKrw;
import static com.podo.helloprice.telegram.domain.product.model.PriceType.CARD;

@UtilityClass
public class ProductCommonResponse {

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
                .append(ProductCommonResponse.toHome())
                .toString();
    }

    public static String descProductDetail(ProductDetailDto product) {

        StringBuilder message = new StringBuilder();
        final Integer productPrice = product.getPrice();
        final Integer productBeforePrice = product.getBeforePrice();

        message.append("<b>")
                .append("최종확인시간 : ")
                .append(DateTimeUtil.dateTimeToString(product.getLastCrawledAt(), DATE_TIME_FORMAT))
                .append("</b>")
                .append("\n")

                .append("<b>가격변동시간</b> : ")
                .append(DateTimeUtil.dateTimeToString(product.getLastUpdateAt(), DATE_TIME_FORMAT))
                .append("\n")
                .append("\n")

                .append("<b>상품링크</b> : ")
                .append(product.getUrl())
                .append("\n")

                .append("<b>상품코드</b> : ")
                .append(product.getProductCode())
                .append("\n")

                .append("<b>")
                .append("상품이름 : ")
                .append(product.getProductName())
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("상품상태 : ")
                .append(product.getSaleStatus().getValue())
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("가격타입 : ★")
                .append(product.getPriceType().value())
                .append(StringUtils.isEmpty(product.getPriceAdditionalInfo()) ? "" : "(" + product.getPriceAdditionalInfo() + ")")
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("이전가격 : ")
                .append(toKrw(productBeforePrice))
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("현재가격 : ")
                .append(toKrw(productPrice))
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("가격변화 : ")
                .append(CalculateUtil.getPercentStringWithPlusMinusSign(productPrice, productBeforePrice))
                .append("</b>");


        return message.toString();
    }

    public static String descProductDetailWithChangeMessage(ProductDetailDto product) {
        return new StringBuilder()
                .append(descProductDetail(product))
                .append("\n")
                .append("\n")
                .append(descProductChange(product))
                .toString();
    }

    public static String descProductChange(ProductDetailDto product) {
        if (product.getAliveStatus().equals(ProductAliveStatus.DEAD)) {
            return "죄송합니다.. <b>상품의 페이지를 확인 할 수 없어요..</b>";
        }

        return descChangeBySaleStatus(product);
    }

    private static String descChangeBySaleStatus(ProductDetailDto product) {

        final ProductSaleStatus saleStatus = product.getSaleStatus();
        switch (saleStatus) {
            case UNKNOWN:
                return "죄송합니다.. <b>상품의 상태를 알 수 없어요..</b>";
            case DISCONTINUE:
                return "죄송합니다.. <b>상품이 단종 됬어요..</b>";
            case NOT_SUPPORT:
                return "죄송합니다.. <b>상품의 가격비교가 중지되었어요..</b>";
            case EMPTY_AMOUNT:
                return "죄송합니다.. <b>상품의 재고가 없어요..</b>";
            case SALE:
                return descSaleStatusChange(product.getPrice(), product.getBeforePrice());
        }

        return "";
    }

    private static String descSaleStatusChange(Integer productPrice, Integer productBeforePrice) {
        final StringBuilder message = new StringBuilder();
        if (productBeforePrice.equals(0)) {
            message.append("야호!  <b>")
                    .append(toKrw(productPrice))
                    .append("</b>으로 다시 판매를 시작했어요!!");
            return message.toString();
        }

        if (productPrice > productBeforePrice) {
            message.append("죄송합니다.. 가격이 <b>")
                    .append(toKrw(productPrice - productBeforePrice))
                    .append("</b> 올랐어요...");
            return message.toString();
        }

        if (productPrice.equals(productBeforePrice)) {
            message.append("<i>아직 가격이 똑같아요! 좀만 더 기다려보세요!</i>");
            return message.toString();
        }

        message.append("야호! 가격이 <b>")
                .append(toKrw(productBeforePrice - productPrice))
                .append("</b> 떨어졌어요!!");

        return message.toString();
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
