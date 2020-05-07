package com.podo.helloprice.telegram.app.menu.product.global;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.core.parser.SaleTypeParser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class ProductDescParameter {

    private final String productCode;
    private final SaleType saleType;

    @Override
    public String toString() {
        return String.format("%s/%s", productCode, SaleTypeParser.kr(saleType));
    }
}
