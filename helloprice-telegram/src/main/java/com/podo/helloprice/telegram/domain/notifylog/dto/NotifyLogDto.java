package com.podo.helloprice.telegram.domain.notifylog.dto;


import com.podo.helloprice.code.model.ProductSaleStatus;
import com.podo.helloprice.core.util.CalculateUtil;
import com.podo.helloprice.telegram.domain.notifylog.model.NotifyLog;
import com.podo.helloprice.telegram.domain.product.dto.ProductDetailDto;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class NotifyLogDto {

    private String productName;
    private String url;
    private Integer price;
    private Integer beforePrice;
    private BigDecimal priceChangeRate;
    private ProductSaleStatus saleStatus;
    private LocalDateTime notifyAt;

    @Builder
    private NotifyLogDto(String productName, String url, Integer price, Integer beforePrice, BigDecimal priceChangeRate, ProductSaleStatus saleStatus, LocalDateTime notifyAt) {
        this.productName = productName;
        this.url = url;
        this.price = price;
        this.beforePrice = beforePrice;
        this.priceChangeRate = priceChangeRate;
        this.saleStatus = saleStatus;
        this.notifyAt = notifyAt;
    }

    public static NotifyLogDto createByProduct(ProductDetailDto productDetail) {
        return NotifyLogDto.builder()
                .productName(productDetail.getProductName())
                .price(productDetail.getPrice())
                .beforePrice(productDetail.getBeforePrice())
                .url(productDetail.getUrl())
                .priceChangeRate(CalculateUtil.getChangePercent(productDetail.getPrice(), productDetail.getBeforePrice()))
                .saleStatus(productDetail.getSaleStatus())
                .notifyAt(LocalDateTime.now())
                .build();
    }

    public NotifyLog toEntity() {
        return NotifyLog.builder()
                .productName(this.productName)
                .url(this.url)
                .price(this.price)
                .beforePrice(this.beforePrice)
                .saleStatus(this.saleStatus)
                .priceChangeRate(this.priceChangeRate)
                .notifyAt(this.notifyAt)
                .build();
    }
}
