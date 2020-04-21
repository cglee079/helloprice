package com.podo.helloprice.product.update.analysis.processor.notify.executor.sale;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.core.model.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.processor.notify.executor.SaleNotifyExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.podo.helloprice.core.model.PriceType.CARD;
import static com.podo.helloprice.core.model.ProductUpdateStatus.UPDATE_SALE_CARD_PRICE;

@RequiredArgsConstructor
@Component
public class SaleOfCardNotifyExecutor extends SaleNotifyExecutor {

    @Override
    public ProductUpdateStatus getProductUpdateStatus() {
        return UPDATE_SALE_CARD_PRICE;
    }

    @Override
    protected PriceType getPriceType() {
        return CARD;
    }
}
