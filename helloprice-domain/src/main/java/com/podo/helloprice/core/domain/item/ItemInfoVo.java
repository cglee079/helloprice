package com.podo.helloprice.core.domain.item;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
public class ItemInfoVo {
    private String itemCode;
    private String itemUrl;
    private String itemDesc;
    private String itemName;
    private String itemImage;
    private ItemSaleStatus itemSaleStatus;
    private Integer itemPrice;


    public ItemInfoVo(String itemCode, String itemUrl, String itemName, String itemDesc, String itemImage, Integer itemPrice, ItemSaleStatus itemSaleStatus) {
        this.itemCode = itemCode;
        this.itemUrl = itemUrl;
        this.itemDesc = itemDesc;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemPrice = itemPrice;
        this.itemSaleStatus = itemSaleStatus;
    }
}