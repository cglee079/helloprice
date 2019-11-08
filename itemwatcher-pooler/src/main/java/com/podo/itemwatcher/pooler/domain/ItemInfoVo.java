package com.podo.itemwatcher.pooler.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
public class ItemInfoVo {
    private String itemCode;
    private String itemName;
    private String itemImage;
    private Integer itemPrice;

    public ItemInfoVo(String itemCode, String itemName, String itemImage, Integer itemPrice) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemPrice = itemPrice;
    }
}
