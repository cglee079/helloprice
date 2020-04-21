package com.podo.helloprice.product.update.analysis.processor.notify.executor.sale;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.core.model.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.processor.notify.executor.AbstractSaleNotifyExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.podo.helloprice.core.model.PriceType.NORMAL;
import static com.podo.helloprice.core.model.ProductUpdateStatus.UPDATE_SALE_NORMAL_PRICE;

@RequiredArgsConstructor
@Component
public class SaleOfNormalNotifyExecutor extends AbstractSaleNotifyExecutor {

    @Override
    public ProductUpdateStatus getProductUpdateStatus() {
        return UPDATE_SALE_NORMAL_PRICE;
    }

    @Override
    protected PriceType getPriceType() {
        return NORMAL;
    }
}
