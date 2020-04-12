package com.podo.helloprice.telegram.app.menu.global;

import com.podo.helloprice.telegram.domain.product.dto.ProductDetailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductCommandTranslator {

    public static List<String> getProductCommands(List<ProductDetailDto> products) {
        return products.stream()
                .map(product -> getProductCommand(product.getProductCode(), product.getProductName()))
                .collect(toList());
    }

    public static String getProductCommand(String productCode, String productName) {
        final String FORMAT = "#%s, %s";
        return String.format(FORMAT, productCode, productName);
    }

    public static String getProductCodeFromCommand(String message) {
        if (StringUtils.isEmpty(message)) {
            return null;
        }

        int index = message.indexOf(",");

        if (index == -1) {
            return null;
        }

        return message.substring(1, index);
    }
}
