package com.podo.helloprice.telegram.app.menu.product;

import com.podo.helloprice.crawl.worker.vo.CrawledProduct;
import com.podo.helloprice.core.model.PriceType;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class ProductTypeCommandTranslator {

    public static final String FORMAT = "#%s/%s";

    public static List<String> encode(CrawledProduct crawledProduct) {
        final String productCode = crawledProduct.getProductCode();

        final List<String> commands = new ArrayList<>();

        commands.add(String.format(FORMAT, productCode, PriceType.NORMAL.value()));

        if(Objects.nonNull(crawledProduct.getCashPrice())) {
            commands.add(String.format(FORMAT, productCode, PriceType.CASH.value()));
        }

        if(Objects.nonNull(crawledProduct.getCardPrice())) {
            commands.add(String.format(FORMAT, productCode, PriceType.CARD.value()));
        }

        return commands;
    }

    public static ProductTypeParameter decode(String command) {
        if (StringUtils.isEmpty(command)) {
            return null;
        }

        int index = command.indexOf("/");

        if (index == -1) {
            return null;
        }

        final String productCode = command.split("/")[0].replace("#","");
        final String priceType = command.split("/")[1];

        return new ProductTypeParameter(productCode, PriceType.from(priceType));
    }
}
