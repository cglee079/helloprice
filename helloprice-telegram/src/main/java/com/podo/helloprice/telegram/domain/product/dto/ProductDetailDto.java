package com.podo.helloprice.telegram.domain.product.dto;

import com.podo.helloprice.code.model.ProductAliveStatus;
import com.podo.helloprice.code.model.ProductSaleStatus;
import com.podo.helloprice.telegram.domain.product.Product;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductDetailDto {

    private Long id;
    private String productName;
    private String productCode;
    private String description;
    private String url;
    private String imageUrl;
    private Integer price;
    private Integer beforePrice;
    private ProductAliveStatus aliveStatus;
    private ProductSaleStatus saleStatus;
    private LocalDateTime lastUpdateAt;
    private LocalDateTime lastPoolAt;

    @Builder
    public ProductDetailDto(Product product) {
        this.id = product.getId();
        this.aliveStatus = product.getAliveStatus();
        this.productCode = product.getProductCode();
        this.productName = product.getProductName();
        this.url = product.getUrl();
        this.description = product.getDescription();
        this.imageUrl = product.getImageUrl();
        this.price = product.getPrice();
        this.saleStatus = product.getSaleStatus();
        this.beforePrice = product.getBeforePrice();
        this.lastUpdateAt = product.getLastUpdatedAt();
        this.lastPoolAt = product.getLastCrawledAt();
    }

}
