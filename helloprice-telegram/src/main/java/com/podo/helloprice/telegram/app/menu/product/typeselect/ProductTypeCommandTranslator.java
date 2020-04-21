package com.podo.helloprice.telegram.app.menu.product.typeselect;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.crawl.worker.vo.CrawledProduct;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.podo.helloprice.core.util.CurrencyUtil.toKRW;
import static java.util.Collections.singletonList;

@UtilityClass
public class ProductTypeCommandTranslator {

    private static final String ALL_PRICE_TYPE = "모든 최저가";

    private static final String TOKEN = "/★";
    private static final String TOKEN_PRICE = "/";
    private static final String PRODUCT_CODE_PREFIX = "#";
    private static final String COMMAND_FORMAT = PRODUCT_CODE_PREFIX + "%s" + TOKEN + "%s" + TOKEN_PRICE + "%s";

    public static List<String> encode(CrawledProduct crawledProduct) {
        final String productCode = crawledProduct.getProductCode();

        final List<String> commands = new ArrayList<>();

        commands.add(String.format(COMMAND_FORMAT, productCode, ALL_PRICE_TYPE, "일괄 등록"));
        commands.add(String.format(COMMAND_FORMAT, productCode, PriceType.NORMAL.kr(), toKRW(crawledProduct.getPrice())));

        if (Objects.nonNull(crawledProduct.getCashPrice())) {
            commands.add(String.format(COMMAND_FORMAT, productCode, PriceType.CASH.kr(), toKRW(crawledProduct.getCashPrice())));
        }

        if (Objects.nonNull(crawledProduct.getCardPrice())) {
            commands.add(String.format(COMMAND_FORMAT, productCode, PriceType.CARD.kr(), toKRW(crawledProduct.getCardPrice())));
        }

        return commands;
    }

    public static ProductTypeParameter decode(String command) {
        if (StringUtils.isEmpty(command)) {
            return null;
        }

        int index = command.indexOf(TOKEN);

        if (index == -1) {
            return null;
        }

        final String productCode = command.split(TOKEN)[0].replace(PRODUCT_CODE_PREFIX, "");
        final String priceType = command.split(TOKEN)[1].split(TOKEN_PRICE)[0];

        if (priceType.equals(ALL_PRICE_TYPE)) {
            return new ProductTypeParameter(productCode, Arrays.asList(PriceType.values()));
        }

        if(Objects.isNull(PriceType.from(priceType))){
            return null;
        }

        return new ProductTypeParameter(productCode, singletonList(PriceType.from(priceType)));
    }
}
