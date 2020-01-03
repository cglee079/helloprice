package com.podo.helloprice.telegram.domain.notifylog;


import com.podo.helloprice.core.domain.notifylog.NotifyLog;
import com.podo.helloprice.core.util.MyCalculateUtils;
import com.podo.helloprice.telegram.domain.item.ItemDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotifyLogDto {

    private String itemName;
    private String itemUrl;
    private Integer itemPrice;
    private Integer itemBeforePrice;
    private Double itemPriceChangeRate;
    private String itemStatus;
    private LocalDateTime notifyAt;

    @Builder
    private NotifyLogDto(String itemName, String itemUrl, Integer itemPrice, Integer itemBeforePrice, Double itemPriceChangeRate, String itemStatus, LocalDateTime notifyAt) {
        this.itemName = itemName;
        this.itemUrl = itemUrl;
        this.itemPrice = itemPrice;
        this.itemBeforePrice = itemBeforePrice;
        this.itemPriceChangeRate = itemPriceChangeRate;
        this.itemStatus = itemStatus;
        this.notifyAt = notifyAt;
    }

    public static NotifyLogDto createByItem(ItemDto.detail itemDetail) {
        final Double changePercent = MyCalculateUtils.getChangePercent(itemDetail.getItemPrice(), itemDetail.getItemBeforePrice());

        return NotifyLogDto.builder()
                .itemName(itemDetail.getItemName())
                .itemPrice(itemDetail.getItemPrice())
                .itemBeforePrice(itemDetail.getItemBeforePrice())
                .itemUrl(itemDetail.getItemUrl())
                .itemPriceChangeRate(Double.parseDouble(String.format("%.2f", changePercent)))
                .itemStatus(itemDetail.getItemSaleStatus().getValue())
                .notifyAt(LocalDateTime.now())
                .build();
    }

    public NotifyLog toEntity() {
        return NotifyLog.builder()
                .itemName(this.itemName)
                .itemUrl(this.itemUrl)
                .itemPrice(this.itemPrice)
                .itemBeforePrice(this.itemBeforePrice)
                .itemStatus(this.itemStatus)
                .itemPriceChangeRate(this.itemPriceChangeRate)
                .notifyAt(this.notifyAt)
                .build();
    }
}
