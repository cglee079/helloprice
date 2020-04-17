package com.podo.helloprice.telegram.app.menu.product;

import com.podo.helloprice.telegram.domain.product.dto.ProductDetailDto;
import com.podo.helloprice.core.model.PriceType;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@UtilityClass
public class ProductDescCommandTranslator {

    public static List<String> encodes(List<ProductDetailDto> products) {
        return products.stream()
                .map(ProductDescCommandTranslator::encode)
                .collect(toList());
    }

    private static String encode(ProductDetailDto product) {
        return String.format("#%s/%s,\n %s", product.getProductCode(), product.getPriceType().value(), product.getProductName());
    }

    public static ProductDescParameter decode(String command) {
        if (StringUtils.isEmpty(command)) {
            return null;
        }

        int index = command.indexOf(",");

        if (index == -1) {
            return null;
        }

        final String product = command.substring(1, index);

        final List<String> strings = Arrays.asList(product.split("/"));
        final String productCode = strings.get(0);
        final PriceType priceType = PriceType.from(strings.get(1));

        if (StringUtils.isEmpty(productCode) || Objects.isNull(priceType)) {
            return null;
        }

        return new ProductDescParameter(productCode, priceType);
    }
}
