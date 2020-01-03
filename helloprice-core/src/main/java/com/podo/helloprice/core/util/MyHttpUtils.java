package com.podo.helloprice.core.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class MyHttpUtils {

    public static String getParamValueFromUrl(String url, String key) {
        final Map<String, String> paramMap = new LinkedHashMap<>();
        final String query = url.substring(url.indexOf("?") + 1);
        final String[] pairs = query.split("&");

        if (pairs.length >= 1) {
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                if (idx != -1) {
                    paramMap.put(pair.substring(0, idx), pair.substring(idx + 1));
                }
            }
        }

        return paramMap.get(key);
    }
}
