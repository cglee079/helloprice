package com.podo.helloprice.crawl.worker.target.danawa;

import com.podo.helloprice.core.util.HttpUrlUtil;
import com.podo.helloprice.core.util.StringUtil;
import com.podo.helloprice.crawl.worker.reader.DocumentPromptReader;
import com.podo.helloprice.crawl.worker.reader.helper.DocumentReaderHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class DanawaProductCodeCrawler {

    private static final String DANAWA_REDIRECT_URL = "https://danawa.page.link";
    private static final List<String> PRODUCT_CODE_PARAM_KEYS = Arrays.asList("pcode", "code");

    private final DocumentPromptReader documentPromptReader;

    public String crawl(String url) {
        if (StringUtil.isEmpty(url)) {
            return null;
        }

        final String realUrl = getRealUrl(url);
        for (String key : PRODUCT_CODE_PARAM_KEYS) {
            final String productCode = HttpUrlUtil.getParamValueFromUrl(realUrl, key);
            if (Objects.nonNull(productCode)) {
                return productCode;
            }
        }

        return null;
    }

    private String getRealUrl(String url) {
        if (!url.contains(DANAWA_REDIRECT_URL)) {
            return url;
        }

        return getRealUrlFromRedirectPage(url);
    }

    private String getRealUrlFromRedirectPage(String url) {
        final Document document = DocumentReaderHelper.crawl(documentPromptReader, url);

        if (Objects.isNull(document)) {
            log.error("리다이렉트 페이지를 가져올 수 없습니다");
            return "";
        }

        final Element canonical = document.select("link[rel=canonical]").first();

        return canonical.attr("href");
    }
}
