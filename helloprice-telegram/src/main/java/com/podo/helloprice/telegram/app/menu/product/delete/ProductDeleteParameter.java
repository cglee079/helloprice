package com.podo.helloprice.telegram.app.menu.product.delete;

import com.podo.helloprice.core.enums.PriceType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class ProductDeleteParameter {

    private final String productCode;
    private final PriceType priceType;

    @Override
    public String toString() {
        return String.format("%s/%s", productCode, priceType.kr());
    }
}
