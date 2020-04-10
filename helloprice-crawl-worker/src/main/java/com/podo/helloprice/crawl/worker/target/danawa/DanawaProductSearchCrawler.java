package com.podo.helloprice.crawl.worker.target.danawa;

import com.podo.helloprice.crawl.worker.reader.DocumentDelayReader;
import com.podo.helloprice.crawl.worker.reader.helper.DocumentReaderHelper;
import com.podo.helloprice.crawl.worker.target.danawa.parser.ProductSearchVoParser;
import com.podo.helloprice.crawl.worker.vo.ProductSearchVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.singletonList;

@Slf4j
@RequiredArgsConstructor
@Component
public class DanawaProductSearchCrawler {

    private static final String DANAWA_PRODUCT_SEARCH_URL = "http://search.danawa.com/mobile/dsearch.php?keyword=";
    private static final String SELECTOR_SEARCH_PRODUCT_LIST = "#productListArea_list > li";
    
    private final DocumentDelayReader documentDelayReader;
    private final DanawaProductCodeCrawler danawaProductCodeCrawler;
    private final ProductSearchVoParser productSearchVoParser = new ProductSearchVoParser();

    public List<ProductSearchVo> crawl(String keyword) {

        final String crawlUrl = DANAWA_PRODUCT_SEARCH_URL + keyword;

        log.debug("CRAWL :: START :: DANAWA '검색' 페이지 ::  상품코드 : {}", keyword);
        log.debug("CRAWL :: START :: URL : {}", crawlUrl);

        final Document document = DocumentReaderHelper.crawl(documentDelayReader, crawlUrl, singletonList(SELECTOR_SEARCH_PRODUCT_LIST));

        if (Objects.isNull(document)) {
            log.debug("CRAWL :: WARN :: 상품 검색 페이지를 가져올 수 없습니다");
            return Collections.emptyList();
        }

        final Elements productSearchResultElements = document.select(SELECTOR_SEARCH_PRODUCT_LIST);
        final List<ProductSearchVo> productSearchResults = productSearchVoParser.parse(danawaProductCodeCrawler, productSearchResultElements);

        return productSearchResults;
    }


}
