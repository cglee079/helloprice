package com.podo.helloprice.crawl.worker.target.danawa;

import com.podo.helloprice.core.domain.item.CrawledItem;
import com.podo.helloprice.core.domain.item.ItemSaleStatus;
import com.podo.helloprice.crawl.worker.exception.FailGetDocumentException;
import com.podo.helloprice.crawl.worker.loader.DelayDocumentLoader;
import com.podo.helloprice.crawl.worker.loader.PromptDocumentLoader;
import com.podo.helloprice.crawl.worker.util.TestUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("다나와 크롤 테스트")
@ExtendWith(MockitoExtension.class)
class DanawaCrawlerTest {

    @Mock
    private PromptDocumentLoader promptDocumentLoader;

    @Mock
    private DelayDocumentLoader delayDocumentLoader;

    @InjectMocks
    private DanawaCrawler danawaCrawler;


    @DisplayName("판매중인 상품 크롤링")
    @Test
    void testCrawlSaleItem() throws FailGetDocumentException {

        //given
        final String itemCode = "7109671";
        final String html = TestUtil.getStringFromResource("document", "danawa_itempage_sale.html");

        final Document document = Jsoup.parse(html);
        given(promptDocumentLoader.getDocument(DanawaCrawlConfig.ItemPage.DANAWA_ITEM_URL + itemCode)).willReturn(document);

        //when
        final CrawledItem crawledItem = danawaCrawler.crawlItem(itemCode);

        //then
        assertThat(crawledItem.getItemCode()).isEqualTo(itemCode);
        assertThat(crawledItem.getItemSaleStatus()).isEqualTo(ItemSaleStatus.SALE);
        assertThat(crawledItem.getItemPrice()).isGreaterThan(0);
        assertThat(crawledItem.getItemName()).isNotEmpty();
    }

    @DisplayName("재고없는 상품 크롤링")
    @Test
    void testCrawlEmptyStockItem() throws FailGetDocumentException {

        //given
        final String itemCode = "5616963";
        final String html = TestUtil.getStringFromResource("document", "danawa_itempage_empty_stock.html");

        final Document document = Jsoup.parse(html);
        given(promptDocumentLoader.getDocument(DanawaCrawlConfig.ItemPage.DANAWA_ITEM_URL + itemCode)).willReturn(document);

        //when
        final CrawledItem crawledItem = danawaCrawler.crawlItem(itemCode);

        //then
        assertThat(crawledItem.getItemCode()).isEqualTo(itemCode);
        assertThat(crawledItem.getItemSaleStatus()).isEqualTo(ItemSaleStatus.EMPTY_AMOUNT);
        assertThat(crawledItem.getItemPrice()).isZero();
        assertThat(crawledItem.getItemName()).isNotEmpty();
    }

    @DisplayName("단종 상품 크롤링")
    @Test
    void testCrawlDiscontinueItem() throws FailGetDocumentException {

        //given
        final String itemCode = "5497265";
        final String html = TestUtil.getStringFromResource("document", "danawa_itempage_discontinue.html");

        final Document document = Jsoup.parse(html);
        given(promptDocumentLoader.getDocument(DanawaCrawlConfig.ItemPage.DANAWA_ITEM_URL + itemCode)).willReturn(document);

        //when
        final CrawledItem crawledItem = danawaCrawler.crawlItem(itemCode);

        //then
        assertThat(crawledItem.getItemCode()).isEqualTo(itemCode);
        assertThat(crawledItem.getItemSaleStatus()).isEqualTo(ItemSaleStatus.DISCONTINUE);
        assertThat(crawledItem.getItemPrice()).isZero();
        assertThat(crawledItem.getItemName()).isNotEmpty();
    }

    @DisplayName("가격비교 중지 상품 크롤링")
    @Test
    void testCrawlNotSupportItem() throws FailGetDocumentException {

        //given
        final String itemCode = "7852171";
        final String html = TestUtil.getStringFromResource("document", "danawa_itempage_not_support.html");

        final Document document = Jsoup.parse(html);
        given(promptDocumentLoader.getDocument(DanawaCrawlConfig.ItemPage.DANAWA_ITEM_URL + itemCode)).willReturn(document);

        //when
        final CrawledItem crawledItem = danawaCrawler.crawlItem(itemCode);

        //then
        assertThat(crawledItem.getItemCode()).isEqualTo(itemCode);
        assertThat(crawledItem.getItemSaleStatus()).isEqualTo(ItemSaleStatus.NOT_SUPPORT);
        assertThat(crawledItem.getItemPrice()).isZero();
        assertThat(crawledItem.getItemName()).isNotEmpty();
    }
}