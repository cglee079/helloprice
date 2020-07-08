package com.podo.helloprice.telegram.app.menu.product.delete;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.core.parser.SaleTypeParser;
import com.podo.helloprice.telegram.domain.product.dto.ProductDto;
import com.podo.helloprice.telegram.domain.productsale.dto.ProductSaleDto;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@UtilityClass
public class ProductDeleteCommandTranslator {

    public static final String TOKEN_PRODUCT_KEY = "/★";
    public static final String TOKEN_PRODUCT_DESC = ".";

    public static List<String> encodes(List<ProductSaleDto> productSales) {
        return productSales.stream()
                .map(ProductDeleteCommandTranslator::encode)
                .collect(toList());
    }

    private static String encode(ProductSaleDto productSale) {
        final ProductDto product = productSale.getProduct();

        return String.format("#%s" + TOKEN_PRODUCT_KEY + "%s" + TOKEN_PRODUCT_DESC + "\n %s", product.getProductCode(), SaleTypeParser.kr(productSale.getSaleType()), product.getProductName());
    }

    public static ProductDeleteParameter decode(String command) {
        if (StringUtils.isEmpty(command)) {
            return null;
        }

        int index = command.indexOf(TOKEN_PRODUCT_DESC);

        if (index == -1) {
            return null;
        }

        final String[] productKey = command.substring(1, index).split(TOKEN_PRODUCT_KEY);
        final String productCode = productKey[0];
        final SaleType saleType = SaleTypeParser.from(productKey[1]);

        if (StringUtils.isEmpty(productCode) || Objects.isNull(saleType)) {
            return null;
        }

        return new ProductDeleteParameter(productCode, saleType);
    }
}
