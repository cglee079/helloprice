package com.podo.helloprice.crawl.worker.reader;


import com.podo.helloprice.crawl.worker.exception.FailReadDocumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class DocumentPromptReader {

    private final String userAgent;
    private final Long readTimeout;

    public Document getDocument(String url) throws FailReadDocumentException {
        final Connection connection = jsoupConnect(url);
        try {
            return connection.get();
        } catch (IOException e) {
            throw new FailReadDocumentException(e);
        }
    }

    private Connection jsoupConnect(String url) {
        return Jsoup.connect(url)
                .userAgent(userAgent)
                .timeout(readTimeout.intValue());
    }
}
