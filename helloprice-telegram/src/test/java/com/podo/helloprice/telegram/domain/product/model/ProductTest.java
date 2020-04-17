//package com.podo.helloprice.telegram.domain.product.model;
//
//import com.podo.helloprice.core.model.ProductSaleStatus;
//import com.podo.helloprice.core.model.ProductAliveStatus;
//import com.podo.helloprice.telegram.domain.userproduct.UserProductNotify;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.time.LocalDateTime;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class ProductTest {
//
//    @DisplayName("생성, By Builder")
//    @Test
//    void testNewInstance01() {
//        //given
//        final String productCode = "productCode";
//        final String productName = "productName";
//        final String description = "productDesc";
//        final String url = "http://productUrl";
//        final String imageUrl = "http://productImage.link";
//        final int productPrice = 1;
//        final ProductSaleStatus productSaleStatus = ProductSaleStatus.SALE;
//        final LocalDateTime lastCrawledAt = LocalDateTime.now();
//
//        //when
//        final Product product = Product.builder()
//                .lastCrawledAt(lastCrawledAt)
//                .productName(productName)
//                .productCode(productCode)
//                .description(description)
//                .url(url)
//                .imageUrl(imageUrl)
//                .saleStatus(productSaleStatus)
//                .build();
//
//        //then
//        assertThat(product.getProductName()).isEqualTo(productName);
//        assertThat(product.getProductCode()).isEqualTo(productCode);
//        assertThat(product.getDescription()).isEqualTo(description);
//        assertThat(product.getProductCode()).isEqualTo(productCode);
//        assertThat(product.getImageUrl()).isEqualTo(imageUrl);
//        assertThat(product.getUrl()).isEqualTo(url);
//        assertThat(product.getSaleStatus()).isEqualTo(productSaleStatus);
//
//        assertThat(product.getDeadCount()).isEqualTo(0);
//        assertThat(product.getAliveStatus()).isEqualTo(ProductAliveStatus.ALIVE);
//
//        assertThat(product.getPrice()).isEqualTo(productPrice);
//        assertThat(product.getBeforePrice()).isEqualTo(productPrice);
//
//        assertThat(product.getLastCrawledAt()).isEqualTo(lastCrawledAt);
//        assertThat(product.getLastUpdatedAt()).isEqualTo(lastCrawledAt);
//        assertThat(product.getLastPublishAt()).isEqualTo(lastCrawledAt);
//    }
//
//
//    @DisplayName("사용자 알림 추가")
//    @Test
//    void testAddProductNotify01() {
//        //given
//        final Product product = Product.builder().build();
//        final UserProductNotify userProductNotify = Mockito.mock(UserProductNotify.class);
//
//        //when
//        product.addUserProductNotify(userProductNotify);
//
//        assertThat(product.getUserProductNotifies()).containsExactly(userProductNotify);
//    }
//
//    @DisplayName("사용자 알림 추가, 상품 PAUSE 상태였을때")
//    @Test
//    void testAddProductNotify02() {
//        //given
//        final Product product = Product.builder().build();
//        ReflectionTestUtils.setField(product, "aliveStatus", ProductAliveStatus.PAUSE);
//
//        final UserProductNotify userProductNotify = Mockito.mock(UserProductNotify.class);
//
//        //when
//        product.addUserProductNotify(userProductNotify);
//
//        assertThat(product.getUserProductNotifies()).containsExactly(userProductNotify);
//        assertThat(product.getAliveStatus()).isEqualTo(ProductAliveStatus.ALIVE);
//    }
//
//    @DisplayName("사용자 알림 삭제")
//    @Test
//    void testRemoveProductNotify01() {
//        //given
//        final Product product = Product.builder().build();
//        final UserProductNotify userProductNotify = Mockito.mock(UserProductNotify.class);
//        product.addUserProductNotify(userProductNotify);
//        product.addUserProductNotify(Mockito.mock(UserProductNotify.class));
//
//        //when
//        product.removeUserProductNotify(userProductNotify);
//
//        //then
//        assertThat(product.getAliveStatus()).isEqualTo(ProductAliveStatus.ALIVE);
//        assertThat(product.getUserProductNotifies().size()).isEqualTo(1);
//    }
//
//    @DisplayName("사용자 알림 삭제후, 어떤 사용자 알림도 없을 때")
//    @Test
//    void testRemoveProductNotify02() {
//        //given
//        final Product product = Product.builder().build();
//        final UserProductNotify userProductNotify = Mockito.mock(UserProductNotify.class);
//        product.addUserProductNotify(userProductNotify);
//
//        //when
//        product.removeUserProductNotify(userProductNotify);
//
//        //then
//        assertThat(product.getAliveStatus()).isEqualTo(ProductAliveStatus.PAUSE);
//    }
//
//
//}