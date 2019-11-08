package com.podo.itemwatcher.telegram.domain;

import com.podo.itemwatcher.core.domain.item.Item;
import com.podo.itemwatcher.core.domain.item.ItemStatus;
import com.podo.itemwatcher.core.domain.user.MenuStatus;
import com.podo.itemwatcher.core.domain.user.User;
import com.podo.itemwatcher.core.domain.user.UserStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ItemDto {


    @Getter
    public static class insert {
        private String itemCode;
        private String itemName;
        private String itemImage;
        private Integer itemPrice;

        @Builder
        public insert(String itemCode, String itemName, String itemImage, Integer itemPrice) {
            this.itemCode = itemCode;
            this.itemName = itemName;
            this.itemImage = itemImage;
            this.itemPrice = itemPrice;
        }

        public Item toEntity() {
            return Item.builder()
                    .itemCode(itemCode)
                    .itemName(itemName)
                    .itemImage(itemImage)
                    .itemBeforePrice(itemPrice)
                    .itemPrice(itemPrice)
                    .itemStatus(ItemStatus.BE)
                    .lastPoolAt(LocalDateTime.now())
                    .build();
        }
    }

}
