package com.podo.sadream.core.domain.item;

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
    private ItemSaleStatus itemSaleStatus;
    private Integer itemPrice;

    public ItemInfoVo(String itemCode, String itemName, String itemImage, Integer itemPrice, ItemSaleStatus itemSaleStatus) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemPrice = itemPrice;
        this.itemSaleStatus = itemSaleStatus;
    }
}
