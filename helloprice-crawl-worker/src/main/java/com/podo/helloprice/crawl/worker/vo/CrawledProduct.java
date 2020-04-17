package com.podo.helloprice.crawl.worker.vo;

import com.podo.helloprice.core.model.ProductSaleStatus;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@EqualsAndHashCode
@Getter
public class CrawledProduct {
    private String productName;
    private String productCode;
    private String url;
    private String description;
    private String imageUrl;
    private ProductSaleStatus saleStatus;
    private Integer price;
    private Integer cashPrice;
    private String cardType;
    private Integer cardPrice;
    private LocalDateTime crawledAt;

    @Builder
    public CrawledProduct(String productCode, String url, String description,
                          String productName, String imageUrl, ProductSaleStatus saleStatus,
                          Integer price, Integer cashPrice, String cardType, Integer cardPrice,
                          LocalDateTime crawledAt) {
        this.productCode = productCode;
        this.url = url;
        this.description = description;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.saleStatus = saleStatus;
        this.price = price;
        this.cashPrice = cashPrice;
        this.cardType = cardType;
        this.cardPrice = cardPrice;
        this.crawledAt = crawledAt;
    }
}

