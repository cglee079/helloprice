package com.podo.helloprice.crawl.agent.domain.product;

import com.podo.helloprice.core.enums.ProductSaleStatus;
import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.crawl.worker.value.CrawledProduct;
import com.podo.helloprice.crawl.worker.value.CrawledProductPrice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Product 단위 테스트, 크롤 데이터에 의한 Product 업데이트")
class ProductUpdateEventByCrawlTest {

    @DisplayName("크롤 상품이, 가격이 변동 됬을 때")
    @Test
    void testUpdateByCrawledProduct01() {
        //given
        final Product product = this.createDefaultProduct(1000);
        final int crawledPrice = Integer.MAX_VALUE;
        final LocalDateTime crawledAt = LocalDateTime.now();
        final CrawledProduct crawledProduct = createCrawledProduct(product, ProductSaleStatus.SALE, crawledPrice, crawledAt);

        //when
        product.updateByCrawledProduct(crawledProduct);

        //then
        assertThat(product.getSaleStatus()).isEqualTo(ProductSaleStatus.SALE);
    }

    @DisplayName("크롤 상품이, 가격이 동일할 때")
    @Test
    void testUpdateByCrawledProduct02() {
        //given
        final int crawledPrice = 1000;
        final Product product = this.createDefaultProduct(crawledPrice);
        final LocalDateTime crawledAt = LocalDateTime.now();
        final CrawledProduct crawledProduct = createCrawledProduct(product, ProductSaleStatus.SALE, crawledPrice, crawledAt);

        //when
        product.updateByCrawledProduct(crawledProduct);

        //then
        assertThat(product.getSaleStatus()).isEqualTo(ProductSaleStatus.SALE);
    }

    @DisplayName("크롤 상품이 재고가 없을 때, 단종 됬을 때, 가격비교 중지 됬을 때, 알 수 없을 때")
    @ParameterizedTest
    @CsvSource({
            "DISCONTINUE, UPDATE_DISCONTINUE",
            "EMPTY_AMOUNT, UPDATE_EMPTY_AMOUNT",
            "NOT_SUPPORT, UPDATE_NOT_SUPPORT",
            "UNKNOWN, UPDATE_UNKNOWN"
    })
    void testUpdateByCrawledProduct03(ProductSaleStatus saleStatus, ProductUpdateStatus updateStatus) {
        //given
        final Product product = this.createDefaultProduct(1000);
        final LocalDateTime crawledAt = LocalDateTime.now();
        final CrawledProduct crawledProduct = createCrawledProduct(product, saleStatus, 0, crawledAt);

        //when
        product.updateByCrawledProduct(crawledProduct);

        //then
        assertThat(product.getSaleStatus()).isEqualTo(saleStatus);
    }

    private CrawledProduct createCrawledProduct(Product product, ProductSaleStatus productSaleStatus, int crawledPrice, LocalDateTime crawledAt) {

        final Map<SaleType, CrawledProductPrice> priceTypeToPrice = new HashMap<>();
        priceTypeToPrice.put(SaleType.NORMAL, new CrawledProductPrice(crawledPrice));
        priceTypeToPrice.put(SaleType.CASH, new CrawledProductPrice(crawledPrice));
        priceTypeToPrice.put(SaleType.CARD, new CrawledProductPrice(crawledPrice, "삼성"));

        return CrawledProduct.builder()
                .productCode(product.getProductCode())
                .productName(product.getProductName())
                .description(product.getDescription())
                .url(product.getUrl())
                .imageUrl(product.getImageUrl())
                .saleStatus(productSaleStatus)
                .crawledAt(crawledAt)
                .build();
    }

    private Product createDefaultProduct(int price) {
        final String productName = "productName";
        final String productCode = "productCode";
        final String url = "http://productUrl";
        final String imageUrl = "http://productImage.link";
        final String description = "productDesc";
        final ProductSaleStatus saleStatus = ProductSaleStatus.SALE;
        final LocalDateTime lastCrawledAt = LocalDateTime.now();


        final Product product = new Product();
        ReflectionTestUtils.setField(product, "productName", productName);
        ReflectionTestUtils.setField(product, "productCode", productCode);
        ReflectionTestUtils.setField(product, "description", description);
        ReflectionTestUtils.setField(product, "url", url);
        ReflectionTestUtils.setField(product, "imageUrl", imageUrl);
        ReflectionTestUtils.setField(product, "saleStatus", saleStatus);
        ReflectionTestUtils.setField(product, "lastCrawledAt", lastCrawledAt);

        return product;
    }

}
