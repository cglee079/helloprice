package com.podo.helloprice.telegram.app.menu.product.searchselect;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

@UtilityClass
public class ProductSearchSelectCommandTranslator {

    public static String encode(String productCode, String productName) {
        return String.format("#%s, %s", productCode, productName);
    }

    public static String decode(String command) {
        if (StringUtils.isEmpty(command)) {
            return null;
        }

        int index = command.indexOf(",");

        if (index == -1) {
            return null;
        }

        return command.substring(1, index);
    }


}
