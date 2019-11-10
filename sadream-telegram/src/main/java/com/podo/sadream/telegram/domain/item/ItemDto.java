package com.podo.sadream.telegram.domain.item;

import com.podo.itemwatcher.core.domain.item.Item;
import com.podo.itemwatcher.core.domain.item.ItemSaleStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class ItemDto {


    @Getter
    public static class insert {
        private String itemCode;
        private String itemUrl;
        private String itemName;
        private String itemImage;
        private Integer itemPrice;
        private ItemSaleStatus itemSaleStatus;

        @Builder
        public insert(String itemCode,
                      String itemUrl, String itemName,
                      String itemImage, Integer itemPrice,
                      ItemSaleStatus itemSaleStatus
        ) {
            this.itemCode = itemCode;
            this.itemUrl = itemUrl;
            this.itemName = itemName;
            this.itemImage = itemImage;
            this.itemPrice = itemPrice;
            this.itemSaleStatus = itemSaleStatus;
        }

        public Item toEntity() {
            return Item.builder()
                    .itemCode(itemCode)
                    .itemUrl(itemUrl)
                    .itemName(itemName)
                    .itemImage(itemImage)
                    .itemPrice(itemPrice)
                    .itemSaleStatus(itemSaleStatus)
                    .lastPoolAt(LocalDateTime.now())
                    .build();
        }
    }


    @Getter
    public static class detail {
        private Long id;
        private String itemCode;
        private String itemUrl;
        private String itemName;
        private String itemImage;
        private Integer itemPrice;
        private Integer itemBeforePrice;
        private ItemSaleStatus itemSaleStatus;
        private LocalDateTime lastUpdateAt;
        private LocalDateTime lastPoolAt;

        @Builder
        public detail(Item item) {
            this.id = item.getId();
            this.itemCode = item.getItemCode();
            this.itemName = item.getItemName();
            this.itemUrl = item.getItemUrl();
            this.itemImage = item.getItemImage();
            this.itemPrice = item.getItemPrice();
            this.itemSaleStatus = item.getItemSaleStatus();
            this.itemBeforePrice = item.getItemBeforePrice();
            this.lastUpdateAt = item.getLastUpdatedAt();
            this.lastPoolAt = item.getLastPoolAt();
        }

    }

}
