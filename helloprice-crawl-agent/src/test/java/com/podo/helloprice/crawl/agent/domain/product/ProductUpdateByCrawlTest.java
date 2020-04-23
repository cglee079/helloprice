//package com.podo.helloprice.crawl.agent.domain.product;
//
//import com.podo.helloprice.core.model.ProductSaleStatus;
//import com.podo.helloprice.crawl.worker.vo.CrawledProduct;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.time.LocalDateTime;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class ProductUpdateByCrawlTest {
//
//
//    @DisplayName("크롤 상품이, 가격이 변동 됬을 때")
//    @Test
//    void testIfSaleAndPriceChange() {
//        //given
//        final Product product = this.createDefaultProduct();
//        final int existedPrice = product.getProductPrice();
//        final int crawledPrice = existedPrice + 1000;
//        final LocalDateTime crawledAt = LocalDateTime.now();
//        final CrawledProduct crawledProduct = createCrawledProduct(product, ProductSaleStatus.SALE, crawledPrice, crawledAt);
//
//        //when
//        product.updateByCrawledProduct(crawledProduct);
//
//        //then
//        assertThat(product.getUpdateStatus()).isEqualTo(UPDATED);
//        assertThat(product.getLastUpdatedAt()).isEqualTo(crawledAt);
//        assertThat(product.getSaleStatus()).isEqualTo(ProductSaleStatus.SALE);
//        assertThat(product.getProductPrice()).isEqualTo(crawledPrice);
//        assertThat(product.getBeforePrice()).isEqualTo(existedPrice);
//    }
//
//    @DisplayName("크롤 상품이, 가격이 동일할때")
//    @Test
//    void testIfSaleAndPriceEqual() {
//        //given
//        final Product product = this.createDefaultProduct();
//        final Integer beforePrice = product.getBeforePrice();
//        final int existedPrice = product.getProductPrice();
//        final int crawledPrice = existedPrice;
//        final LocalDateTime crawledAt = LocalDateTime.now();
//        final CrawledProduct crawledProduct = createCrawledProduct(product, ProductSaleStatus.SALE, crawledPrice, crawledAt);
//
//        //when
//        product.updateByCrawledProduct(crawledProduct);
//
//        //then
//        assertThat(product.getLastCrawledAt()).isEqualTo(crawledAt);
//        assertThat(product.getSaleStatus()).isEqualTo(ProductSaleStatus.SALE);
//        assertThat(product.getProductPrice()).isEqualTo(crawledPrice);
//        assertThat(product.getBeforePrice()).isEqualTo(beforePrice);
//    }
//
//    @DisplayName("크롤 상품이 재고가 없거나, 가격 비교 중지되거나, 가격을 알수 없을때")
//    @ParameterizedTest
//    @ValueSource(strings = {"EMPTY_AMOUNT", "DISCONTINUE", "NOT_SUPPORT"})
//    void testIfEmptyAmount(String productSaleStatusString) {
//        //given
//        final ProductSaleStatus productSaleStatus = ProductSaleStatus.valueOf(productSaleStatusString);
//        final Product product = this.createDefaultProduct();
//        final int existedPrice = product.getProductPrice();
//        final int crawledPrice = 0;
//        final LocalDateTime crawledAt = LocalDateTime.now();
//        final CrawledProduct crawledProduct = createCrawledProduct(product, productSaleStatus, crawledPrice, crawledAt);
//
//        //when
//        product.updateByCrawledProduct(crawledProduct);
//
//        //then
//        assertThat(product.getSaleStatus()).isEqualTo(productSaleStatus);
//        assertThat(product.getProductPrice()).isEqualTo(crawledPrice);
//        assertThat(product.getBeforePrice()).isEqualTo(existedPrice);
//    }
//
//    private CrawledProduct createCrawledProduct(Product product, ProductSaleStatus productSaleStatus, int crawledPrice, LocalDateTime crawledAt) {
//        return CrawledProduct.builder()
//                .productCode(product.getProductCode())
//                .productName(product.getProductName())
//                .description(product.getDescription())
//                .url(product.getUrl())
//                .imageUrl(product.getImageUrl())
//                .saleStatus(productSaleStatus)
//                .price(crawledPrice)
//                .crawledAt(crawledAt)
//                .build();
//    }
//
//    private Product createDefaultProduct() {
//        final String productName = "productName";
//        final String productCode = "productCode";
//        final String url = "http://productUrl";
//        final String imageUrl = "http://productImage.link";
//        final String description = "productDesc";
//        final int price = 1;
//        final int beforePrice = 2;
//        final ProductSaleStatus saleStatus = ProductSaleStatus.SALE;
//        final LocalDateTime lastCrawledAt = LocalDateTime.now();
//
//        final Product product = new Product();
//        ReflectionTestUtils.setField(product, "productName", productName);
//        ReflectionTestUtils.setField(product, "productCode", productCode);
//        ReflectionTestUtils.setField(product, "description", description);
//        ReflectionTestUtils.setField(product, "url", url);
//        ReflectionTestUtils.setField(product, "imageUrl", imageUrl);
//        ReflectionTestUtils.setField(product, "price", price);
//        ReflectionTestUtils.setField(product, "beforePrice", beforePrice);
//        ReflectionTestUtils.setField(product, "saleStatus", saleStatus);
//        ReflectionTestUtils.setField(product, "lastCrawledAt", lastCrawledAt);
//
//        return product;
//    }
//
//}
