package com.podo.helloprice.crawl.worker.target.danawa;

import com.podo.helloprice.core.enums.PriceType;
import com.podo.helloprice.crawl.worker.exception.FailReadDocumentException;
import com.podo.helloprice.crawl.worker.reader.DocumentPromptReader;
import com.podo.helloprice.crawl.worker.util.TestUtil;
import com.podo.helloprice.crawl.worker.value.CrawledProduct;
import com.podo.helloprice.core.enums.ProductSaleStatus;
import org.assertj.core.api.Assertions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.podo.helloprice.core.enums.PriceType.CASH;
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
    void testCrawlSaleProduct() throws FailReadDocumentException {

        //given
        final String productCode = "7109671";
        final String html = TestUtil.getStringFromResource("document", "danawa_product_page_sale.html");
        final LocalDateTime crawledAt = LocalDateTime.now();

        final Document document = Jsoup.parse(html);
        given(documentPromptReader.getDocument(DanawaProductCrawler.DANAWA_PRODUCT_URL + productCode)).willReturn(document);

        //when
        final CrawledProduct crawledProduct = danawaProductCrawler.crawl(productCode, crawledAt);

        //then
        assertThat(crawledProduct.getProductCode()).isEqualTo(productCode);
        assertThat(crawledProduct.getSaleStatus()).isEqualTo(ProductSaleStatus.SALE);
        assertThat(crawledProduct.getProductPriceByType(PriceType.NORMAL).getPrice()).isEqualTo(56850);
        assertThat(crawledProduct.getProductPriceByType(CASH).getPrice()).isEqualTo(54500);
        assertThat(crawledProduct.getProductPriceByType(PriceType.CARD).getPrice()).isEqualTo(55330);
        assertThat(crawledProduct.getProductPriceByType(PriceType.CARD).getAdditionalInfo()).isEqualTo("삼성");
        assertThat(crawledProduct.getProductName()).isNotEmpty();
        assertThat(crawledProduct.getCrawledAt()).isEqualTo(crawledAt);
    }

    @DisplayName("재고없는 상품 크롤링")
    @Test
    void testCrawlEmptyStockProduct() throws FailReadDocumentException {

        //given
        final String productCode = "5616963";
        final String html = TestUtil.getStringFromResource("document", "danawa_product_page_empty_stock.html");
        final LocalDateTime crawledAt = LocalDateTime.now();

        final Document document = Jsoup.parse(html);
        given(documentPromptReader.getDocument(DanawaProductCrawler.DANAWA_PRODUCT_URL + productCode)).willReturn(document);

        //when
        final CrawledProduct crawledProduct = danawaProductCrawler.crawl(productCode, crawledAt);

        //then
        assertThat(crawledProduct.getProductCode()).isEqualTo(productCode);
        assertThat(crawledProduct.getSaleStatus()).isEqualTo(ProductSaleStatus.EMPTY_AMOUNT);
        assertThat(crawledProduct.getProductName()).isNotEmpty();
        assertThat(crawledProduct.getCrawledAt()).isEqualTo(crawledAt);
    }

    @DisplayName("단종 상품 크롤링")
    @Test
    void testCrawlDiscontinueProduct() throws FailReadDocumentException {

        //given
        final String productCode = "5497265";
        final String html = TestUtil.getStringFromResource("document", "danawa_product_page_discontinue.html");
        final LocalDateTime crawledAt = LocalDateTime.now();

        final Document document = Jsoup.parse(html);
        given(documentPromptReader.getDocument(DanawaProductCrawler.DANAWA_PRODUCT_URL + productCode)).willReturn(document);

        //when
        final CrawledProduct crawledProduct = danawaProductCrawler.crawl(productCode, crawledAt);

        //then
        assertThat(crawledProduct.getProductName()).isNotEmpty();
        assertThat(crawledProduct.getProductCode()).isEqualTo(productCode);
        Assertions.assertThat(crawledProduct.getSaleStatus()).isEqualTo(ProductSaleStatus.DISCONTINUE);
        assertThat(crawledProduct.getCrawledAt()).isEqualTo(crawledAt);
    }

    @DisplayName("가격비교 중지 상품 크롤링")
    @Test
    void testCrawlNotSupportProduct() throws FailReadDocumentException {

        //given
        final String productCode = "7852171";
        final String html = TestUtil.getStringFromResource("document", "danawa_product_page_not_support.html");
        final LocalDateTime crawledAt = LocalDateTime.now();

        final Document document = Jsoup.parse(html);
        given(documentPromptReader.getDocument(DanawaProductCrawler.DANAWA_PRODUCT_URL + productCode)).willReturn(document);

        //when
        final CrawledProduct crawledProduct = danawaProductCrawler.crawl(productCode, crawledAt);

        //then
        assertThat(crawledProduct.getProductName()).isNotEmpty();
        assertThat(crawledProduct.getProductCode()).isEqualTo(productCode);
        assertThat(crawledProduct.getSaleStatus()).isEqualTo(ProductSaleStatus.NOT_SUPPORT);
        assertThat(crawledProduct.getCrawledAt()).isEqualTo(crawledAt);
    }
}
