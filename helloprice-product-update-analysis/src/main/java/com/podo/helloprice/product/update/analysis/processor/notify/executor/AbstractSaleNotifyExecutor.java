package com.podo.helloprice.product.update.analysis.processor.notify.executor;

import com.podo.helloprice.core.enums.PriceType;
import com.podo.helloprice.core.enums.UserStatus;
import com.podo.helloprice.core.util.CalculateUtil;
import com.podo.helloprice.core.util.CurrencyUtil;
import com.podo.helloprice.product.update.analysis.domain.product.application.ProductReadService;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductDetailDto;
import com.podo.helloprice.product.update.analysis.domain.user.UserReadService;
import com.podo.helloprice.product.update.analysis.domain.user.UserDto;
import com.podo.helloprice.product.update.analysis.domain.userproduct.application.UserProductNotifyReadService;
import com.podo.helloprice.product.update.analysis.infra.mq.message.EmailNotifyMessage;
import com.podo.helloprice.product.update.analysis.infra.mq.message.TelegramNotifyMessage;
import com.podo.helloprice.product.update.analysis.processor.notify.Notifier;
import com.podo.helloprice.product.update.analysis.processor.notify.helper.EmailContentCreator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.podo.helloprice.core.util.CalculateUtil.getChangePercent;
import static com.podo.helloprice.core.util.CurrencyUtil.toKRW;
import static java.math.BigDecimal.valueOf;

public abstract class AbstractSaleNotifyExecutor implements NotifyExecutor {

    @Autowired
    private Notifier notifier;

    @Autowired
    private ProductReadService productReadService;

    @Autowired
    private UserReadService userReadService;

    @Autowired
    private UserProductNotifyReadService userProductNotifyReadService;

    protected abstract PriceType getPriceType();

    @Override
    public boolean execute(Long productId) {
        final PriceType priceType = this.getPriceType();

        final ProductDetailDto product = productReadService.findByProductId(productId, priceType);
        final List<Long> userIds = userProductNotifyReadService.findUserIdsByProductIdAndPriceType(productId, priceType);
        final List<UserDto> users = userReadService.findByUserIdsAndUserStatus(userIds, UserStatus.ALIVE);

        final String imageUrl = product.getImageUrl();
        final Integer price = product.getPrice();
        final Integer prevPrice = product.getPrevPrice();

        if (satisfiedSendResaleNotify(price, prevPrice)) {
            notifyToUsers(users, imageUrl, getResaleNotifyTitle(product), getResaleNotifyContents(product));
            return true;
        }

        if (satisfiedSendZeroPriceNotify(price)) {
            notifyToUsers(users, imageUrl, getZeroPriceNotifyTitle(product), getZeroPriceNotifyContents(product));
            return true;
        }

        if (satisfiedSendLowestPriceNotify(price, prevPrice)) {
            notifyToUsers(users, imageUrl, getLowestPriceNotifyTitle(product), getLowestPriceNotifyContents(product));
            return true;
        }

        return false;
    }

    private void notifyToUsers(List<UserDto> users, String imageUrl, String title, String contents) {
        for (UserDto user : users) {
            notifier.notify(TelegramNotifyMessage.create(user.getTelegramId(), imageUrl, contents));
            notifier.notify(EmailNotifyMessage.create(user.getEmail(), user.getUsername(), title, EmailContentCreator.create(imageUrl, contents)));
        }
    }

    private boolean satisfiedSendResaleNotify(Integer price, Integer prevPrice) {
        return price > 0 && prevPrice == 0;
    }

    private boolean satisfiedSendZeroPriceNotify(Integer price) {
        return price == 0;
    }

    private boolean satisfiedSendLowestPriceNotify(Integer price, Integer prevPrice) {
        return price < prevPrice && getChangePercent(price, prevPrice).compareTo(valueOf(-1)) < 0;
    }

    private String getResaleNotifyTitle(ProductDetailDto product) {
        return new StringBuilder()
                .append("'")
                .append(product.getProductName())
                .append("' 상품이 ")
                .append(toKRW(product.getPrice()))
                .append(" 으로 다시 판매를 시작했어요.")
                .toString();
    }

    private String getResaleNotifyContents(ProductDetailDto product) {
        return new StringBuilder()
                .append("<b>")
                .append("상품이름 : ")
                .append(product.getProductName())
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("현재가격 : ")
                .append(CurrencyUtil.toKRW(product.getPrice()))
                .append("</b>")
                .append("\n")

                .append("---------------------------------------\n")

                .append("<b>")
                .append("야호! 해당 상품은 다시 판매를 시작했어요\n")
                .append("</b>")
                .append("\n")
                .append("\n")

                .append(ProductDescribe.descProductDetailWithChangeMessage(product))
                .toString();
    }


    private String getZeroPriceNotifyTitle(ProductDetailDto product) {
        return new StringBuilder()
                .append(product.getProductName())
                .append("' 상품의 ")
                .append(product.getPriceType().kr())
                .append("는 판매가 진행되지 않고 있습니다")
                .toString();
    }

    private String getZeroPriceNotifyContents(ProductDetailDto product) {
        return new StringBuilder()
                .append("<b>")
                .append("상품이름 : ")
                .append(product.getProductName())
                .append("</b>")
                .append("\n")

                .append("---------------------------------------\n")
                .append("\n")

                .append("<b>")
                .append(product.getPriceType().kr())
                .append("</b>는 판매가 진행되지 않고 있어요..\n")
                .append("\n")

                .toString();
    }


    private String getLowestPriceNotifyTitle(ProductDetailDto product) {
        return new StringBuilder()
                .append("'")
                .append(product.getProductName())
                .append("' 상품의 최저가격이  ")
                .append(toKRW(product.getPrice()))
                .append(" 으로 갱신되었습니다.")
                .toString();
    }

    private String getLowestPriceNotifyContents(ProductDetailDto product) {
        final Integer price = product.getPrice();
        final Integer prevPrice = product.getPrevPrice();

        return new StringBuilder()
                .append("<b>")
                .append("상품이름 : ")
                .append(product.getProductName())
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("현재가격 : ")
                .append(CurrencyUtil.toKRW(price))
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("가격변화 : ")
                .append(CalculateUtil.getSignPercent(price, prevPrice))
                .append("</b>")
                .append("\n")

                .append("---------------------------------------\n")
                .append("\n")

                .append("<b>")
                .append("해당 상품은 최저가가 갱신되었습니다!!\n")
                .append("</b>")
                .append("\n")

                .append(ProductDescribe.descProductDetailWithChangeMessage(product))
                .toString();
    }


}
