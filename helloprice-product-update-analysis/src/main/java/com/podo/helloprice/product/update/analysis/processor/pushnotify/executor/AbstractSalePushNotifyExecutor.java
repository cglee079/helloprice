package com.podo.helloprice.product.update.analysis.processor.pushnotify.executor;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.core.enums.UserStatus;
import com.podo.helloprice.core.parser.SaleTypeParser;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductDto;
import com.podo.helloprice.product.update.analysis.domain.productsale.ProductSaleReadService;
import com.podo.helloprice.product.update.analysis.domain.productsale.dto.ProductSaleDto;
import com.podo.helloprice.product.update.analysis.domain.user.application.UserReadService;
import com.podo.helloprice.product.update.analysis.domain.user.dto.UserDto;
import com.podo.helloprice.product.update.analysis.domain.usernotify.application.UserNotifyReadService;
import com.podo.helloprice.product.update.analysis.processor.pushnotify.PushTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.podo.helloprice.core.util.CalculateUtil.getChangePercent;
import static com.podo.helloprice.core.util.CurrencyUtil.toKRW;
import static java.math.BigDecimal.valueOf;

public abstract class AbstractSalePushNotifyExecutor implements PushNotifyExecutor {

    @Autowired
    private ProductSaleReadService productSaleReadService;

    @Autowired
    private UserReadService userReadService;

    @Autowired
    private UserNotifyReadService userNotifyReadService;

    protected abstract SaleType getSaleType();

    @Override
    public PushTarget execute(Long productId) {
        final ProductSaleDto productSale = productSaleReadService.findByProductIdAndSaleType(productId, this.getSaleType());
        final ProductDto product = productSale.getProduct();

        final List<Long> userIds = userNotifyReadService.findUserIdsByProductSaleId(productSale.getId());
        final List<UserDto> users = userReadService.findByIdsAndUserStatus(userIds, UserStatus.ALIVE);

        final String imageUrl = product.getImageUrl();
        final String redirectUrl = product.getUrl();
        final Integer price = productSale.getPrice();
        final Integer prevPrice = productSale.getPrevPrice();

        if (satisfiedSendResaleNotify(price, prevPrice)) {
            return new PushTarget(users, imageUrl, redirectUrl, getResaleContents(productSale));
        }

        if (satisfiedSendZeroPriceNotify(price)) {
            return new PushTarget(users, imageUrl, redirectUrl, getZeroPriceContents(productSale));
        }

        if (satisfiedSendLowestPriceNotify(price, prevPrice)) {
            return new PushTarget(users, imageUrl, redirectUrl, getLowestPriceContents(productSale));
        }

        return PushTarget.EMPTY;
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

    private String getResaleContents(ProductSaleDto productSale) {
        final ProductDto product = productSale.getProduct();

        return new StringBuilder()
                .append("'")
                .append(product.getProductName())
                .append("' 상품이 ")
                .append(toKRW(productSale.getPrice()))
                .append(" 으로 다시 판매를 시작했어요.")
                .toString();
    }


    private String getZeroPriceContents(ProductSaleDto productSale) {
        final ProductDto product = productSale.getProduct();

        return new StringBuilder()
                .append(product.getProductName())
                .append("' 상품의 ")
                .append(SaleTypeParser.kr(productSale.getSaleType()))
                .append("는 판매가 진행되지 않고 있습니다")
                .toString();
    }


    private String getLowestPriceContents(ProductSaleDto productSale) {
        final ProductDto product = productSale.getProduct();

        return new StringBuilder()
                .append("'")
                .append(product.getProductName())
                .append("' 상품의 최저가격이  ")
                .append(toKRW(productSale.getPrice()))
                .append(" 으로 갱신되었습니다.")
                .toString();
    }


}
