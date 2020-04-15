//package com.podo.helloprice.core.domain.item;
//
//
//import com.podo.helloprice.core.domain.item.model.ProductSaleStatus;
//import com.podo.helloprice.core.domain.item.model.ProductUpdateStatus;
//import com.podo.helloprice.core.domain.item.vo.CrawledProduct;
//import com.podo.helloprice.core.domain.product.Product;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//
//import java.time.LocalDateTime;
//
//import static com.podo.helloprice.core.domain.item.model.ProductSaleStatus.*;
//import static com.podo.helloprice.core.domain.item.model.ProductUpdateStatus.BE;
//import static com.podo.helloprice.core.domain.item.model.ProductUpdateStatus.UPDATED;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DisplayName("상품 테스트, 크롤에 의한 업데이트")
//class ProductUpdateByCrawlTest {
//
//
//    @DisplayName("크롤 상품이, 가격이 변동 됬을 때")
//    @Test
//    void testIfSaleAndPriceChange(){
//        //given
//        final Product item = this.createDefaultProduct();
//        final int existedPrice = item.getProductPrice();
//        final int crawledPrice = existedPrice + 1000;
//        final LocalDateTime crawledAt = LocalDateTime.now();
//        final CrawledProduct crawledProduct = createCrawledProduct(item, SALE, crawledPrice, crawledAt);
//
//        //when
//        item.updateByCrawledProduct(crawledProduct);
//
//        //then
//        assertThat(item.getProductUpdateStatus()).isEqualTo(UPDATED);
//        assertThat(item.getLastUpdatedAt()).isEqualTo(crawledAt);
//        assertThat(item.getProductSaleStatus()).isEqualTo(SALE);
//        assertThat(item.getProductBeforePrice()).isEqualTo(existedPrice);
//        assertThat(item.getProductPrice()).isEqualTo(crawledPrice);
//    }
//
//    @DisplayName("크롤 상품이, 가격이 동일할때")
//    @Test
//    void testIfSaleAndPriceEqual(){
//        //given
//        final Product item = this.createDefaultProduct();
//        final int existedPrice = item.getProductPrice();
//        final int crawledPrice = existedPrice;
//        final LocalDateTime crawledAt = LocalDateTime.now();
//        final CrawledProduct crawledProduct = createCrawledProduct(item, SALE, crawledPrice, crawledAt);
//
//        //when
//        item.updateByCrawledProduct(crawledProduct);
//
//        //then
//        assertThat(item.getProductUpdateStatus()).isEqualTo(BE);
//        assertThat(item.getLastUpdatedAt()).isEqualTo(crawledAt);
//        assertThat(item.getProductSaleStatus()).isEqualTo(SALE);
//        assertThat(item.getProductBeforePrice()).isEqualTo(existedPrice);
//        assertThat(item.getProductPrice()).isEqualTo(crawledPrice);
//    }
//
//    @DisplayName("크롤 상품이 재고가 없거나, 가격 비교 중지되거나, 가격을 알수 없을때")
//    @ParameterizedTest
//    @ValueSource(strings = {"EMPTY_AMOUNT", "DISCONTINUE", "NOT_SUPPORT"})
//    void testIfEmptyAmount(String itemSaleStatusString){
//        //given
//        final ProductSaleStatus itemSaleStatus = valueOf(itemSaleStatusString);
//        final Product item = this.createDefaultProduct();
//        final int existedPrice = item.getProductPrice();
//        final int crawledPrice = 0;
//        final LocalDateTime crawledAt = LocalDateTime.now();
//        final CrawledProduct crawledProduct = createCrawledProduct(item, itemSaleStatus, crawledPrice, crawledAt);
//
//        //when
//        item.updateByCrawledProduct(crawledProduct);
//
//        //then
//        assertThat(item.getProductUpdateStatus()).isEqualTo(UPDATED);
//        assertThat(item.getLastUpdatedAt()).isEqualTo(crawledAt);
//        assertThat(item.getProductSaleStatus()).isEqualTo(itemSaleStatus);
//        assertThat(item.getProductBeforePrice()).isEqualTo(existedPrice);
//        assertThat(item.getProductPrice()).isEqualTo(crawledPrice);
//    }
//
//
//    private CrawledProduct createCrawledProduct(Product item, ProductSaleStatus itemSaleStatus, int crawledPrice, LocalDateTime crawledAt) {
//        return CrawledProduct.builder()
//                .itemCode(item.getProductCode())
//                .itemName(item.getProductName())
//                .itemDesc(item.getProductDesc())
//                .itemUrl(item.getProductUrl())
//                .itemImage(item.getProductImage())
//                .itemSaleStatus(itemSaleStatus)
//                .itemPrice(crawledPrice)
//                .crawledAt(crawledAt)
//                .build();
//    }
//
//    private Product createDefaultProduct(){
//        final String itemCode = "itemCode";
//        final String itemName = "itemName";
//        final String itemImage = "http://itemImage.link";
//        final String itemDesc = "itemDesc";
//        final String itemUrl = "http://itemUrl";
//        final int itemPrice = 1;
//        final ProductSaleStatus itemSaleStatus = SALE;
//        final LocalDateTime lastCrawledAt = LocalDateTime.now();
//
//        //when
//        return Product.builder()
//                .lastCrawledAt(lastCrawledAt)
//                .itemDesc(itemDesc)
//                .itemCode(itemCode)
//                .itemImage(itemImage)
//                .itemName(itemName)
//                .itemPrice(itemPrice)
//                .itemSaleStatus(itemSaleStatus)
//                .itemUrl(itemUrl)
//                .build();
//    }
//}
