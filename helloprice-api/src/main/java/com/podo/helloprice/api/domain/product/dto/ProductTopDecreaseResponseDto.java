package com.podo.helloprice.api.domain.product.dto;

import com.podo.helloprice.api.domain.product.model.Product;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductTopDecreaseResponseDto {

    private Long id;
    private String productName;
    private String productCode;
    private String url;
    private String imageUrl;
    private Double priceChangeRate;
    private LocalDateTime lastUpdateAt;

    public ProductTopDecreaseResponseDto(Product product, Double priceChangeRate) {
        this.id = product.getId();
        this.productCode = product.getProductCode();
        this.productName = product.getProductName();
        this.url = product.getUrl();
        this.imageUrl = product.getImageUrl();
        this.lastUpdateAt = product.getLastCrawledAt();
        this.priceChangeRate = priceChangeRate;
    }
}
