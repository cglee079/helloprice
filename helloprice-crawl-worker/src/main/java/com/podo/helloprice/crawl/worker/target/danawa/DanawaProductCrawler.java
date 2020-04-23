package com.podo.helloprice.crawl.worker.target.danawa;

import com.podo.helloprice.crawl.worker.reader.DocumentPromptReader;
import com.podo.helloprice.crawl.worker.reader.helper.DocumentReaderHelper;
import com.podo.helloprice.crawl.worker.target.danawa.parser.CrawledProductParser;
import com.podo.helloprice.crawl.worker.value.CrawledProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class DanawaProductCrawler {

    public static final String DANAWA_PRODUCT_URL = "http://prod.danawa.com/info/?pcode=";

    private final DocumentPromptReader documentPromptReader;

    private final CrawledProductParser crawledProductParser = new CrawledProductParser();

    public CrawledProduct crawl(String productCode, LocalDateTime crawledAt) {

        final String productUrl = DANAWA_PRODUCT_URL + productCode;

        log.debug("CRAWL :: START :: DANAWA '상품' 페이지 ::  상품코드 : {}", productCode);
        log.debug("CRAWL :: START :: URL : {}", productUrl);

        final Document document = DocumentReaderHelper.crawl(documentPromptReader, productUrl);
        if (Objects.isNull(document)) {
            log.error("CRAWL :: ERROR :: 상품 상세 페이지를 가져올 수 없습니다");
            return null;
        }

        final CrawledProduct crawledProduct = crawledProductParser.parse(document, productCode, productUrl, crawledAt);

        log.debug("CRAWL :: END :: 상품 정보확인, '{}'", crawledProduct);

        return crawledProduct;

    }

}
