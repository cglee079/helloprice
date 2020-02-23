package com.podo.helloprice.core.domain.item;


import com.podo.helloprice.core.domain.item.model.ItemSaleStatus;
import com.podo.helloprice.core.domain.item.model.ItemUpdateStatus;
import com.podo.helloprice.core.domain.item.vo.CrawledItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static com.podo.helloprice.core.domain.item.model.ItemSaleStatus.*;
import static com.podo.helloprice.core.domain.item.model.ItemUpdateStatus.BE;
import static com.podo.helloprice.core.domain.item.model.ItemUpdateStatus.UPDATED;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 테스트, 크롤에 의한 업데이트")
class ItemUpdateByCrawlTest {


    @DisplayName("크롤 상품이, 가격이 변동 됬을 때")
    @Test
    void testIfSaleAndPriceChange(){
        //given
        final Item item = this.createDefaultItem();
        final int existedPrice = item.getItemPrice();
        final int crawledPrice = existedPrice + 1000;
        final LocalDateTime crawledAt = LocalDateTime.now();
        final CrawledItem crawledItem = createCrawledItem(item, SALE, crawledPrice, crawledAt);

        //when
        item.updateByCrawledItem(crawledItem);

        //then
        assertThat(item.getItemUpdateStatus()).isEqualTo(UPDATED);
        assertThat(item.getLastUpdatedAt()).isEqualTo(crawledAt);
        assertThat(item.getItemSaleStatus()).isEqualTo(SALE);
        assertThat(item.getItemBeforePrice()).isEqualTo(existedPrice);
        assertThat(item.getItemPrice()).isEqualTo(crawledPrice);
    }

    @DisplayName("크롤 상품이, 가격이 동일할때")
    @Test
    void testIfSaleAndPriceEqual(){
        //given
        final Item item = this.createDefaultItem();
        final int existedPrice = item.getItemPrice();
        final int crawledPrice = existedPrice;
        final LocalDateTime crawledAt = LocalDateTime.now();
        final CrawledItem crawledItem = createCrawledItem(item, SALE, crawledPrice, crawledAt);

        //when
        item.updateByCrawledItem(crawledItem);

        //then
        assertThat(item.getItemUpdateStatus()).isEqualTo(BE);
        assertThat(item.getLastUpdatedAt()).isEqualTo(crawledAt);
        assertThat(item.getItemSaleStatus()).isEqualTo(SALE);
        assertThat(item.getItemBeforePrice()).isEqualTo(existedPrice);
        assertThat(item.getItemPrice()).isEqualTo(crawledPrice);
    }

    @DisplayName("크롤 상품이 재고가 없거나, 가격 비교 중지되거나, 가격을 알수 없을때")
    @ParameterizedTest
    @ValueSource(strings = {"EMPTY_AMOUNT", "DISCONTINUE", "NOT_SUPPORT"})
    void testIfEmptyAmount(String itemSaleStatusString){
        //given
        final ItemSaleStatus itemSaleStatus = valueOf(itemSaleStatusString);
        final Item item = this.createDefaultItem();
        final int existedPrice = item.getItemPrice();
        final int crawledPrice = 0;
        final LocalDateTime crawledAt = LocalDateTime.now();
        final CrawledItem crawledItem = createCrawledItem(item, itemSaleStatus, crawledPrice, crawledAt);

        //when
        item.updateByCrawledItem(crawledItem);

        //then
        assertThat(item.getItemUpdateStatus()).isEqualTo(UPDATED);
        assertThat(item.getLastUpdatedAt()).isEqualTo(crawledAt);
        assertThat(item.getItemSaleStatus()).isEqualTo(itemSaleStatus);
        assertThat(item.getItemBeforePrice()).isEqualTo(existedPrice);
        assertThat(item.getItemPrice()).isEqualTo(crawledPrice);
    }



    private CrawledItem createCrawledItem(Item item, ItemSaleStatus itemSaleStatus, int crawledPrice, LocalDateTime crawledAt) {
        return CrawledItem.builder()
                .itemCode(item.getItemCode())
                .itemName(item.getItemName())
                .itemDesc(item.getItemDesc())
                .itemUrl(item.getItemUrl())
                .itemImage(item.getItemImage())
                .itemSaleStatus(itemSaleStatus)
                .itemPrice(crawledPrice)
                .crawledAt(crawledAt)
                .build();
    }

    private Item createDefaultItem(){
        final String itemCode = "itemCode";
        final String itemName = "itemName";
        final String itemImage = "http://itemImage.link";
        final String itemDesc = "itemDesc";
        final String itemUrl = "http://itemUrl";
        final int itemPrice = 1;
        final ItemSaleStatus itemSaleStatus = SALE;
        final LocalDateTime lastCrawledAt = LocalDateTime.now();

        //when
        return Item.builder()
                .lastCrawledAt(lastCrawledAt)
                .itemDesc(itemDesc)
                .itemCode(itemCode)
                .itemImage(itemImage)
                .itemName(itemName)
                .itemPrice(itemPrice)
                .itemSaleStatus(itemSaleStatus)
                .itemUrl(itemUrl)
                .build();
    }
}
