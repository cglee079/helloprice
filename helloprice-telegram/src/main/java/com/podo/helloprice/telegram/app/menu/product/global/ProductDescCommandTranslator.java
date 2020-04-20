package com.podo.helloprice.telegram.app.menu.product.global;

import com.podo.helloprice.telegram.domain.product.dto.ProductOnePriceTypeDto;
import com.podo.helloprice.core.model.PriceType;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@UtilityClass
public class ProductDescCommandTranslator {

    public static final String TOKEN_PRODUCT_KEY = "/★";
    public static final String TOKEN_PRODUCT_DESC = ".";

    public static List<String> encodes(List<ProductOnePriceTypeDto> products) {
        return products.stream()
                .map(ProductDescCommandTranslator::encode)
                .collect(toList());
    }

    private static String encode(ProductOnePriceTypeDto product) {
        return String.format("#%s" + TOKEN_PRODUCT_KEY + "%s" + TOKEN_PRODUCT_DESC + "\n %s", product.getProductCode(), product.getPriceType().kr(), product.getProductName());
    }

    public static ProductDescParameter decode(String command) {
        if (StringUtils.isEmpty(command)) {
            return null;
        }

        int index = command.indexOf(TOKEN_PRODUCT_DESC);

        if (index == -1) {
            return null;
        }

        final String[] productKey = command.substring(1, index).split(TOKEN_PRODUCT_KEY);
        final String productCode = productKey[0];
        final PriceType priceType = PriceType.from(productKey[1]);

        if (StringUtils.isEmpty(productCode) || Objects.isNull(priceType)) {
            return null;
        }

        return new ProductDescParameter(productCode, priceType);
    }
}
