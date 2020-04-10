package com.podo.helloprice.crawl.worker.target.danawa;

import com.podo.helloprice.crawl.worker.exception.FailReadDocumentException;
import com.podo.helloprice.crawl.worker.reader.DocumentPromptReader;
import com.podo.helloprice.crawl.worker.util.TestUtil;
import com.podo.helloprice.crawl.worker.vo.CrawledProduct;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.podo.helloprice.code.model.ProductSaleStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("다나와 상품 페이지 크롤 테스트")
@ExtendWith(MockitoExtension.class)
class DanawaProductCrawlerTest {

    @Mock
    private DocumentPromptReader documentPromptReader;

    @InjectMocks
    private DanawaProductCrawler danawaProductCrawler;

    @DisplayName("판매중인 상품 크롤링")
    @Test
    void testCrawlSaleItem() throws FailReadDocumentException {

        //given
        final String productCode = "7109671";
        final String html = TestUtil.getStringFromResource("document", "danawa_product_page_sale.html");
        final LocalDateTime crawledAt = LocalDateTime.now();

        final Document document = Jsoup.parse(html);
        given(documentPromptReader.getDocument(DanawaProductCrawler.DANAWA_PRODUCT_URL + productCode)).willReturn(document);

        //when
        final CrawledProduct crawledItem = danawaProductCrawler.crawl(productCode, crawledAt);

        //then
        assertThat(crawledItem.getProductCode()).isEqualTo(productCode);
        assertThat(crawledItem.getSaleStatus()).isEqualTo(SALE);
        assertThat(crawledItem.getPrice()).isGreaterThan(0);
        assertThat(crawledItem.getProductName()).isNotEmpty();
        assertThat(crawledItem.getCrawledAt()).isEqualTo(crawledAt);
    }

    @DisplayName("재고없는 상품 크롤링")
    @Test
    void testCrawlEmptyStockItem() throws FailReadDocumentException {

        //given
        final String productCode = "5616963";
        final String html = TestUtil.getStringFromResource("document", "danawa_product_page_empty_stock.html");
        final LocalDateTime crawledAt = LocalDateTime.now();

        final Document document = Jsoup.parse(html);
        given(documentPromptReader.getDocument(DanawaProductCrawler.DANAWA_PRODUCT_URL + productCode)).willReturn(document);

        //when
        final CrawledProduct crawledItem = danawaProductCrawler.crawl(productCode, crawledAt);

        //then
        assertThat(crawledItem.getProductCode()).isEqualTo(productCode);
        assertThat(crawledItem.getSaleStatus()).isEqualTo(EMPTY_AMOUNT);
        assertThat(crawledItem.getPrice()).isZero();
        assertThat(crawledItem.getProductName()).isNotEmpty();
        assertThat(crawledItem.getCrawledAt()).isEqualTo(crawledAt);
    }

    @DisplayName("단종 상품 크롤링")
    @Test
    void testCrawlDiscontinueItem() throws FailReadDocumentException {

        //given
        final String productCode = "5497265";
        final String html = TestUtil.getStringFromResource("document", "danawa_product_page_discontinue.html");
        final LocalDateTime crawledAt = LocalDateTime.now();

        final Document document = Jsoup.parse(html);
        given(documentPromptReader.getDocument(DanawaProductCrawler.DANAWA_PRODUCT_URL + productCode)).willReturn(document);

        //when
        final CrawledProduct crawledItem = danawaProductCrawler.crawl(productCode, crawledAt);

        //then
        assertThat(crawledItem.getProductName()).isNotEmpty();
        assertThat(crawledItem.getProductCode()).isEqualTo(productCode);
        assertThat(crawledItem.getSaleStatus()).isEqualTo(DISCONTINUE);
        assertThat(crawledItem.getPrice()).isZero();
        assertThat(crawledItem.getCrawledAt()).isEqualTo(crawledAt);
    }

    @DisplayName("가격비교 중지 상품 크롤링")
    @Test
    void testCrawlNotSupportItem() throws FailReadDocumentException {

        //given
        final String productCode = "7852171";
        final String html = TestUtil.getStringFromResource("document", "danawa_product_page_not_support.html");
        final LocalDateTime crawledAt = LocalDateTime.now();

        final Document document = Jsoup.parse(html);
        given(documentPromptReader.getDocument(DanawaProductCrawler.DANAWA_PRODUCT_URL + productCode)).willReturn(document);

        //when
        final CrawledProduct crawledItem = danawaProductCrawler.crawl(productCode, crawledAt);

        //then
        assertThat(crawledItem.getProductName()).isNotEmpty();
        assertThat(crawledItem.getProductCode()).isEqualTo(productCode);
        assertThat(crawledItem.getSaleStatus()).isEqualTo(NOT_SUPPORT);
        assertThat(crawledItem.getPrice()).isZero();
        assertThat(crawledItem.getCrawledAt()).isEqualTo(crawledAt);
    }
}