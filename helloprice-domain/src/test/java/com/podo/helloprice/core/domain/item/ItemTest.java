package com.podo.helloprice.core.domain.item;

import com.podo.helloprice.core.domain.item.model.ProductSaleStatus;
import com.podo.helloprice.core.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static com.podo.helloprice.core.domain.item.model.ProductSaleStatus.SALE;
import static com.podo.helloprice.core.domain.item.model.ProductStatus.*;
import static com.podo.helloprice.core.domain.item.model.ProductUpdateStatus.BE;
import static com.podo.helloprice.core.domain.item.model.ProductUpdateStatus.UPDATED;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 테스트")
class ProductTest {


    @DisplayName("생성, By Builder")
    @Test
    void testNewInstance01(){
        //given
        final String itemCode = "itemCode";
        final String itemName = "itemName";
        final String itemImage = "http://itemImage.link";
        final String itemDesc = "itemDesc";
        final String itemUrl = "http://itemUrl";
        final int itemPrice = 1;
        final ProductSaleStatus itemSaleStatus = SALE;
        final LocalDateTime lastCrawledAt = LocalDateTime.now();

        //when
        final Product item = Product.builder()
                .lastCrawledAt(lastCrawledAt)
                .itemDesc(itemDesc)
                .itemCode(itemCode)
                .itemImage(itemImage)
                .itemName(itemName)
                .itemPrice(itemPrice)
                .itemSaleStatus(itemSaleStatus)
                .itemUrl(itemUrl)
                .build();

        //then
        assertThat(item.getProductName()).isEqualTo(itemName);
        assertThat(item.getProductCode()).isEqualTo(itemCode);
        assertThat(item.getProductDesc()).isEqualTo(itemDesc);
        assertThat(item.getProductCode()).isEqualTo(itemCode);
        assertThat(item.getProductImage()).isEqualTo(itemImage);
        assertThat(item.getProductUrl()).isEqualTo(itemUrl);
        assertThat(item.getProductSaleStatus()).isEqualTo(itemSaleStatus);

        assertThat(item.getDeadCount()).isEqualTo(0);
        assertThat(item.getProductAliveStatus()).isEqualTo(ALIVE);
        assertThat(item.getProductUpdateStatus()).isEqualTo(BE);

        assertThat(item.getProductBeforePrice()).isEqualTo(itemPrice);
        assertThat(item.getProductPrice()).isEqualTo(itemPrice);

        assertThat(item.getLastCrawledAt()).isEqualTo(lastCrawledAt);
        assertThat(item.getLastUpdatedAt()).isEqualTo(lastCrawledAt);
        assertThat(item.getLastPublishAt()).isEqualTo(lastCrawledAt);

    }

    @DisplayName("DEAD_COUNT 증가")
    @Test
    void testIncreaseDeadCount(){
        //given
        final Product item = Product.builder().build();
        final LocalDateTime lastCrawledAt = LocalDateTime.now();

        //when
        item.increaseDeadCount(100, lastCrawledAt);

        //then
        assertThat(item.getDeadCount()).isEqualTo(1);
    }

    @DisplayName("DEAD_COUNT 초과")
    @Test
    void testIncreaseDeadCountOverMax(){
        //given
        final int itemPrice = 1000;
        final Product item = Product.builder().itemPrice(itemPrice).build();
        final LocalDateTime lastCrawledAt = LocalDateTime.now();

        //when
        item.increaseDeadCount(0, lastCrawledAt);

        //then
        assertThat(item.getDeadCount()).isEqualTo(1);
        assertThat(item.getProductBeforePrice()).isEqualTo(itemPrice);
        assertThat(item.getProductPrice()).isEqualTo(0);
        assertThat(item.getProductAliveStatus()).isEqualTo(DEAD);
        assertThat(item.getProductUpdateStatus()).isEqualTo(UPDATED);
        assertThat(item.getLastUpdatedAt()).isEqualTo(lastCrawledAt);
    }

    @DisplayName("사용자 알림 추가")
    @Test
    void testAddProductNotify01(){
        //given
        final Product item = Product.builder().build();
        final UserProductNotify userProductNotify = Mockito.mock(UserProductNotify.class);

        //when
        item.addUserProductNotify(userProductNotify);

        assertThat(item.getUserProductNotifies()).containsExactly(userProductNotify);
    }

    @DisplayName("사용자 알림 추가, 상품 PAUSE 상태였을때")
    @Test
    void testAddProductNotify02(){
        //given
        final Product item = Product.builder().build();
        ReflectionTestUtils.setField(item, "itemStatus", PAUSE);

        final UserProductNotify userProductNotify = Mockito.mock(UserProductNotify.class);

        //when
        item.addUserProductNotify(userProductNotify);

        assertThat(item.getUserProductNotifies()).containsExactly(userProductNotify);
        assertThat(item.getProductAliveStatus()).isEqualTo(ALIVE);
    }

    @DisplayName("사용자 알림 삭제")
    @Test
    void testRemoveProductNotify01(){
        //given
        final Product item = Product.builder().build();
        final UserProductNotify userProductNotify = Mockito.mock(UserProductNotify.class);
        item.addUserProductNotify(userProductNotify);
        item.addUserProductNotify(Mockito.mock(UserProductNotify.class));

        //when
        item.removeUserProductNotify(userProductNotify);

        //then
        assertThat(item.getProductAliveStatus()).isEqualTo(ALIVE);
        assertThat(item.getUserProductNotifies().size()).isEqualTo(1);
    }

    @DisplayName("사용자 알림 삭제후, 어떤 사용자 알림도 없을 때")
    @Test
    void testRemoveProductNotify02(){
        //given
        final Product item = Product.builder().build();
        final UserProductNotify userProductNotify = Mockito.mock(UserProductNotify.class);
        item.addUserProductNotify(userProductNotify);

        //when
        item.removeUserProductNotify(userProductNotify);

        //then
        assertThat(item.getProductAliveStatus()).isEqualTo(PAUSE);
    }

    @DisplayName("메세지 퍼블리시 시간 갱신")
    @Test
    void testUpdateLastPublishAt(){
        //given
        final LocalDateTime lastPublishAt = LocalDateTime.now();
        final Product item = Product.builder().build();

        //when
        item.updateLastPublishAt(lastPublishAt);

        //then
        assertThat(item.getLastPublishAt()).isEqualTo(lastPublishAt);
    }


}