package com.podo.helloprice.telegram.app.menu.product.typeselect;

import com.podo.helloprice.core.enums.PriceType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class ProductTypeParameter {

    private final String productCode;
    private final List<PriceType> priceTypes;

}
