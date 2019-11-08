package com.podo.itemwatcher.core.domain.item;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
public class ItemInfoVo {
    private String itemName;
    private String itemImage;
    private Integer itemPrice;

    public ItemInfoVo(String itemName, String itemImage, Integer itemPrice) {
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemPrice = itemPrice;
    }
}
