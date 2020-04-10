package com.podo.helloprice.crawl.worker.reader.helper;

import com.podo.helloprice.crawl.worker.reader.DocumentDelayReader;
import com.podo.helloprice.crawl.worker.reader.DocumentPromptReader;
import com.podo.helloprice.crawl.worker.exception.FailReadDocumentException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;

import java.util.List;

@Slf4j
@UtilityClass
public class DocumentReaderHelper {

    public static Document crawl(DocumentPromptReader documentPromptReader, String url) {
        try {
            return documentPromptReader.getDocument(url);
        } catch (FailReadDocumentException e) {
            log.error("", e);
            return null;
        }
    }

    public static Document crawl(DocumentDelayReader documentDelayReader, String url, List<String> waitElementSelectors) {
        try {
            return documentDelayReader.getDocument(url, waitElementSelectors);
        } catch (FailReadDocumentException e) {
            log.error("", e);
            return null;
        }
    }
}
