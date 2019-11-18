package com.podo.helloprice.pooler.loader;


import com.podo.helloprice.pooler.exception.FailGetDocumentException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class PromptDocumentLoader {

    @Value("${pooler.useragent}")
    private String userAgent;

    @Value("${pooler.timeout}")
    private Integer timeout;

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
                .userAgent(userAgent)
                .timeout(timeout);
    }

}
