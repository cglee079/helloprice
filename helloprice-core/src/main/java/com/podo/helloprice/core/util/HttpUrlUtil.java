package com.podo.helloprice.core.util;

import lombok.experimental.UtilityClass;

import java.util.LinkedHashMap;
import java.util.Map;

@UtilityClass
public class HttpUrlUtil {

    public static String getParamValueFromUrl(String url, String findKey) {
        final Map<String, String> paramMap = new LinkedHashMap<>();
        final String query = url.substring(url.indexOf("?") + 1);
        final String[] pairs = query.split("&");

        if (pairs.length >= 1) {
            for (String pair : pairs) {
                int index = pair.indexOf("=");
                if (index != -1) {
                    String key = pair.substring(0, index);
                    String value = pair.substring(index + 1);
                    paramMap.put(key, value);
                }
            }
        }

        return paramMap.get(findKey);
    }
}
