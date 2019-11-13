package com.podo.helloprice.pooler;


import com.podo.helloprice.pooler.exception.FailGetDocumentException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
class JsoupDocumentLoader {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36";
    private static final Integer MAX_TIMEOUT = 15000;

    public Document getDocument(String url) throws FailGetDocumentException {
        final Connection connection = jsoupConnect(url);

        try {
            return connection.get();
        } catch (IOException e) {
            throw new FailGetDocumentException(e);
        }
    }

    private Connection jsoupConnect(String url) {

        return Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .timeout(MAX_TIMEOUT);
    }

}
