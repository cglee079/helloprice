package com.podo.helloprice.product.update.analysis.processor.notify.executor;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.core.parser.SaleTypeParser;
import com.podo.helloprice.core.enums.UserStatus;
import com.podo.helloprice.core.util.CalculateUtil;
import com.podo.helloprice.core.util.CurrencyUtil;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductDto;
import com.podo.helloprice.product.update.analysis.domain.productsale.ProductSaleReadService;
import com.podo.helloprice.product.update.analysis.domain.productsale.dto.ProductSaleDto;
import com.podo.helloprice.product.update.analysis.domain.tuser.TUserReadService;
import com.podo.helloprice.product.update.analysis.domain.tuser.TUserDto;
import com.podo.helloprice.product.update.analysis.domain.tusernotify.application.TUserNotifyReadService;
import com.podo.helloprice.product.update.analysis.processor.notify.NotifyTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.podo.helloprice.core.util.CalculateUtil.getChangePercent;
import static com.podo.helloprice.core.util.CurrencyUtil.toKRW;
import static java.math.BigDecimal.valueOf;

public abstract class AbstractSaleNotifyExecutor implements NotifyExecutor {

    @Autowired
    private ProductSaleReadService productSaleReadService;

    @Autowired
    private TUserReadService tUserReadService;

    @Autowired
    private TUserNotifyReadService tUserNotifyReadService;

    protected abstract SaleType getSaleType();

    @Override
    public NotifyTarget execute(Long productId) {
        final ProductSaleDto productSale = productSaleReadService.findByProductIdAndSaleType(productId, this.getSaleType());
        final ProductDto product = productSale.getProduct();

        final List<Long> userIds = tUserNotifyReadService.findUserIdsByProductSaleId(productSale.getId());
        final List<TUserDto> users = tUserReadService.findByUserIdsAndUserStatus(userIds, UserStatus.ALIVE);

        final String imageUrl = product.getImageUrl();
        final Integer price = productSale.getPrice();
        final Integer prevPrice = productSale.getPrevPrice();

        if (satisfiedSendResaleNotify(price, prevPrice)) {
            return new NotifyTarget(users, imageUrl, getResaleNotifyTitle(productSale), getResaleNotifyContents(productSale));
        }

        if (satisfiedSendZeroPriceNotify(price)) {
            return new NotifyTarget(users, imageUrl, getZeroPriceNotifyTitle(productSale), getZeroPriceNotifyContents(productSale));
        }

        if (satisfiedSendLowestPriceNotify(price, prevPrice)) {
            return new NotifyTarget(users, imageUrl, getLowestPriceNotifyTitle(productSale), getLowestPriceNotifyContents(productSale));
        }

        return NotifyTarget.EMPTY;
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

    private String getResaleNotifyTitle(ProductSaleDto productSale) {
        final ProductDto product = productSale.getProduct();

        return new StringBuilder()
                .append("'")
                .append(product.getProductName())
                .append("/")
                .append(SaleTypeParser.kr(productSale.getSaleType()))
                .append("' 상품이 ")
                .append(toKRW(productSale.getPrice()))
                .append(" 으로 다시 판매를 시작했어요.")
                .toString();
    }

    private String getResaleNotifyContents(ProductSaleDto productSale) {
        final ProductDto product = productSale.getProduct();

        return new StringBuilder()
                .append("<b>")
                .append("상품이름 : ")
                .append(product.getProductName())
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("현재가격 : ")
                .append(CurrencyUtil.toKRW(productSale.getPrice()))
                .append("</b>")
                .append("\n")

                .append("---------------------------------------\n")

                .append("<b>")
                .append("야호! 해당 상품은 다시 판매를 시작했어요\n")
                .append("</b>")
                .append("\n")
                .append("\n")

                .append(ProductDescribe.descProductDetailWithChangeMessage(productSale))
                .toString();
    }


    private String getZeroPriceNotifyTitle(ProductSaleDto productSale) {
        final ProductDto product = productSale.getProduct();

        return new StringBuilder()
                .append(product.getProductName())
                .append("/")
                .append(SaleTypeParser.kr(productSale.getSaleType()))
                .append("' 상품의 ")
                .append(SaleTypeParser.kr(productSale.getSaleType()))
                .append("는 판매가 진행되지 않고 있습니다")
                .toString();
    }

    private String getZeroPriceNotifyContents(ProductSaleDto productSale) {
        final ProductDto product = productSale.getProduct();

        return new StringBuilder()
                .append("<b>")
                .append("상품이름 : ")
                .append(product.getProductName())
                .append("</b>")
                .append("\n")

                .append("---------------------------------------\n")
                .append("\n")

                .append("<b>")
                .append(SaleTypeParser.kr(productSale.getSaleType()))
                .append("</b>는 판매가 진행되지 않고 있어요..\n")
                .append("\n")

                .toString();
    }


    private String getLowestPriceNotifyTitle(ProductSaleDto productSale) {
        final ProductDto product = productSale.getProduct();

        return new StringBuilder()
                .append("'")
                .append(product.getProductName())
                .append("/")
                .append(SaleTypeParser.kr(productSale.getSaleType()))
                .append("' 상품의 최저가격이  ")
                .append(toKRW(productSale.getPrice()))
                .append(" 으로 갱신되었습니다.")
                .toString();
    }

    private String getLowestPriceNotifyContents(ProductSaleDto productSale) {
        final ProductDto product = productSale.getProduct();
        final Integer price = productSale.getPrice();
        final Integer prevPrice = productSale.getPrevPrice();

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

                .append(ProductDescribe.descProductDetailWithChangeMessage(productSale))
                .toString();
    }


}
