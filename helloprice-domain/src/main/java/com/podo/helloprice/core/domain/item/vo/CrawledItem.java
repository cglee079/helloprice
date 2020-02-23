package com.podo.helloprice.core.domain.item.vo;

import com.podo.helloprice.core.domain.item.model.ItemSaleStatus;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@EqualsAndHashCode
@Getter
public class CrawledItem {
    private String itemCode;
    private String itemUrl;
    private String itemDesc;
    private String itemName;
    private String itemImage;
    private ItemSaleStatus itemSaleStatus;
    private Integer itemPrice;
    private LocalDateTime crawledAt;

    @Builder
    public CrawledItem(String itemCode, String itemUrl, String itemDesc, String itemName, String itemImage, ItemSaleStatus itemSaleStatus, Integer itemPrice, LocalDateTime crawledAt) {
        this.itemCode = itemCode;
        this.itemUrl = itemUrl;
        this.itemDesc = itemDesc;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemSaleStatus = itemSaleStatus;
        this.itemPrice = itemPrice;
        this.crawledAt = crawledAt;
    }
}
