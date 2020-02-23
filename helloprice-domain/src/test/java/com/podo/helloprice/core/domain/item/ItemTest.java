package com.podo.helloprice.core.domain.item;

import com.podo.helloprice.core.domain.item.model.ItemSaleStatus;
import com.podo.helloprice.core.domain.useritem.UserItemNotify;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static com.podo.helloprice.core.domain.item.model.ItemSaleStatus.SALE;
import static com.podo.helloprice.core.domain.item.model.ItemStatus.*;
import static com.podo.helloprice.core.domain.item.model.ItemUpdateStatus.BE;
import static com.podo.helloprice.core.domain.item.model.ItemUpdateStatus.UPDATED;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 테스트")
class ItemTest {


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
        final ItemSaleStatus itemSaleStatus = SALE;
        final LocalDateTime lastCrawledAt = LocalDateTime.now();

        //when
        final Item item = Item.builder()
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
        assertThat(item.getItemName()).isEqualTo(itemName);
        assertThat(item.getItemCode()).isEqualTo(itemCode);
        assertThat(item.getItemDesc()).isEqualTo(itemDesc);
        assertThat(item.getItemCode()).isEqualTo(itemCode);
        assertThat(item.getItemImage()).isEqualTo(itemImage);
        assertThat(item.getItemUrl()).isEqualTo(itemUrl);
        assertThat(item.getItemSaleStatus()).isEqualTo(itemSaleStatus);

        assertThat(item.getDeadCount()).isEqualTo(0);
        assertThat(item.getItemStatus()).isEqualTo(ALIVE);
        assertThat(item.getItemUpdateStatus()).isEqualTo(BE);

        assertThat(item.getItemBeforePrice()).isEqualTo(itemPrice);
        assertThat(item.getItemPrice()).isEqualTo(itemPrice);

        assertThat(item.getLastCrawledAt()).isEqualTo(lastCrawledAt);
        assertThat(item.getLastUpdatedAt()).isEqualTo(lastCrawledAt);
        assertThat(item.getLastPublishAt()).isEqualTo(lastCrawledAt);

    }

    @DisplayName("DEAD_COUNT 증가")
    @Test
    void testIncreaseDeadCount(){
        //given
        final Item item = Item.builder().build();
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
        final Item item = Item.builder().itemPrice(itemPrice).build();
        final LocalDateTime lastCrawledAt = LocalDateTime.now();

        //when
        item.increaseDeadCount(0, lastCrawledAt);

        //then
        assertThat(item.getDeadCount()).isEqualTo(1);
        assertThat(item.getItemBeforePrice()).isEqualTo(itemPrice);
        assertThat(item.getItemPrice()).isEqualTo(0);
        assertThat(item.getItemStatus()).isEqualTo(DEAD);
        assertThat(item.getItemUpdateStatus()).isEqualTo(UPDATED);
        assertThat(item.getLastUpdatedAt()).isEqualTo(lastCrawledAt);
    }

    @DisplayName("사용자 알림 추가")
    @Test
    void testAddItemNotify01(){
        //given
        final Item item = Item.builder().build();
        final UserItemNotify userItemNotify = Mockito.mock(UserItemNotify.class);

        //when
        item.addUserItemNotify(userItemNotify);

        assertThat(item.getUserItemNotifies()).containsExactly(userItemNotify);
    }

    @DisplayName("사용자 알림 추가, 상품 PAUSE 상태였을때")
    @Test
    void testAddItemNotify02(){
        //given
        final Item item = Item.builder().build();
        ReflectionTestUtils.setField(item, "itemStatus", PAUSE);

        final UserItemNotify userItemNotify = Mockito.mock(UserItemNotify.class);

        //when
        item.addUserItemNotify(userItemNotify);

        assertThat(item.getUserItemNotifies()).containsExactly(userItemNotify);
        assertThat(item.getItemStatus()).isEqualTo(ALIVE);
    }

    @DisplayName("사용자 알림 삭제")
    @Test
    void testRemoveItemNotify01(){
        //given
        final Item item = Item.builder().build();
        final UserItemNotify userItemNotify = Mockito.mock(UserItemNotify.class);
        item.addUserItemNotify(userItemNotify);
        item.addUserItemNotify(Mockito.mock(UserItemNotify.class));

        //when
        item.removeUserItemNotify(userItemNotify);

        //then
        assertThat(item.getItemStatus()).isEqualTo(ALIVE);
        assertThat(item.getUserItemNotifies().size()).isEqualTo(1);
    }

    @DisplayName("사용자 알림 삭제후, 어떤 사용자 알림도 없을 때")
    @Test
    void testRemoveItemNotify02(){
        //given
        final Item item = Item.builder().build();
        final UserItemNotify userItemNotify = Mockito.mock(UserItemNotify.class);
        item.addUserItemNotify(userItemNotify);

        //when
        item.removeUserItemNotify(userItemNotify);

        //then
        assertThat(item.getItemStatus()).isEqualTo(PAUSE);
    }

    @DisplayName("메세지 퍼블리시 시간 갱신")
    @Test
    void testUpdateLastPublishAt(){
        //given
        final LocalDateTime lastPublishAt = LocalDateTime.now();
        final Item item = Item.builder().build();

        //when
        item.updateLastPublishAt(lastPublishAt);

        //then
        assertThat(item.getLastPublishAt()).isEqualTo(lastPublishAt);
    }


}