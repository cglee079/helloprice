package com.podo.helloprice.product.update.analysis.processor.notify.executor;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.core.util.CalculateUtil;
import com.podo.helloprice.core.util.CurrencyUtil;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductDetailDto;
import com.podo.helloprice.product.update.analysis.domain.user.dto.UserDto;
import com.podo.helloprice.product.update.analysis.facade.NotifyTarget;
import com.podo.helloprice.product.update.analysis.facade.NotifyTargetFacadeReadService;
import com.podo.helloprice.product.update.analysis.infra.mq.message.EmailNotifyMessage;
import com.podo.helloprice.product.update.analysis.infra.mq.message.TelegramNotifyMessage;
import com.podo.helloprice.product.update.analysis.processor.notify.Notifier;
import com.podo.helloprice.product.update.analysis.processor.notify.helper.EmailContentCreator;
import com.podo.helloprice.product.update.analysis.processor.notify.helper.ProductDescribe;
import org.springframework.beans.factory.annotation.Autowired;

import static com.podo.helloprice.core.util.CalculateUtil.getChangePercent;
import static com.podo.helloprice.core.util.CurrencyUtil.toKRW;
import static java.math.BigDecimal.valueOf;

public abstract class SaleNotifyExecutor implements NotifyExecutor {

    @Autowired
    private Notifier notifier;

    @Autowired
    private NotifyTargetFacadeReadService notifyTargetFacadeReadService;

    protected abstract PriceType getPriceType();

    @Override
    public boolean execute(Long productId) {
        final NotifyTarget notifyTarget = notifyTargetFacadeReadService.get(productId, getPriceType());
        final ProductDetailDto product = notifyTarget.getProduct();

        final String imageUrl = product.getImageUrl();
        final String title = getNotifyTitle(product);
        final String contents = getNotifyContents(product);

        if (satisfiedSendNotify(product.getPrice(), product.getBeforePrice())) {
            for (UserDto user : notifyTarget.getUsers()) {
                notifier.notify(TelegramNotifyMessage.create(user.getTelegramId(), imageUrl, contents));
                notifier.notify(EmailNotifyMessage.create(user.getEmail(), user.getUsername(), title, EmailContentCreator.create(imageUrl, contents)));
            }

            return true;
        }

        return false;
    }

    private boolean satisfiedSendNotify(Integer price, Integer beforePrice){
        return price < beforePrice && getChangePercent(price, beforePrice).compareTo(valueOf(-1)) < 0;
    }

    private String getNotifyTitle(ProductDetailDto product) {
        return new StringBuilder()
                .append("'")
                .append(product.getProductName())
                .append("' 상품의 최저가격이  ")
                .append(toKRW(product.getPrice()))
                .append(" 으로 갱신되었습니다.")
                .toString();
    }

    private String getNotifyContents(ProductDetailDto product) {
        final Integer price = product.getPrice();
        final Integer beforePrice = product.getBeforePrice();

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
                .append(CalculateUtil.getPercentStringWithPlusMinusSign(price, beforePrice))
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
