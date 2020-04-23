package com.podo.helloprice.product.update.analysis.processor.notify.executor.sale;

import com.podo.helloprice.core.enums.PriceType;
import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.processor.notify.executor.AbstractSaleNotifyExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.podo.helloprice.core.enums.PriceType.CARD;
import static com.podo.helloprice.core.enums.ProductUpdateStatus.UPDATE_SALE_CARD_PRICE;

@RequiredArgsConstructor
@Component
public class SaleOfCardNotifyExecutor extends AbstractSaleNotifyExecutor {

    @Override
    public ProductUpdateStatus getProductUpdateStatus() {
        return UPDATE_SALE_CARD_PRICE;
    }

    @Override
    protected PriceType getPriceType() {
        return CARD;
    }
}
