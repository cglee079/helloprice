package com.podo.itemwatcher.core.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class MyHttpUtils {

    public static String getParamValue(String urlStr, String key) {
        Map<String, String> params = new LinkedHashMap<>();

        String query = urlStr.substring(urlStr.indexOf("?") + 1);
        String[] pairs = query.split("&");
        if (pairs.length >= 1) {
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                if (idx != -1) {
                    params.put(pair.substring(0, idx), pair.substring(idx + 1));
                }
            }
        }

        return params.get(key);
    }
}
