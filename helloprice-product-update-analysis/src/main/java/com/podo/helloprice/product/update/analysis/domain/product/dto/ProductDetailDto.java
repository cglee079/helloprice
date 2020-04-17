package com.podo.helloprice.product.update.analysis.domain.product.dto;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.core.model.ProductAliveStatus;
import com.podo.helloprice.core.model.ProductSaleStatus;
import com.podo.helloprice.product.update.analysis.domain.product.model.Product;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductDetailDto {

    private Long id;
    private String productName;
    private String productCode;
    private String description;
    private PriceType priceType;
    private String url;
    private String imageUrl;
    private Integer price;
    private Integer beforePrice;
    private String priceAdditionalInfo;
    private ProductAliveStatus aliveStatus;
    private ProductSaleStatus saleStatus;
    private LocalDateTime lastUpdateAt;
    private LocalDateTime lastCrawledAt;

    @Builder
    public ProductDetailDto(Product product, PriceType priceType) {
        this.id = product.getId();
        this.aliveStatus = product.getAliveStatus();
        this.productCode = product.getProductCode();
        this.productName = product.getProductName();
        this.url = product.getUrl();
        this.description = product.getDescription();
        this.imageUrl = product.getImageUrl();
        this.saleStatus = product.getSaleStatus();
        this.lastCrawledAt = product.getLastCrawledAt();
        this.priceType = priceType;
        this.price = product.getPrice(priceType);
        this.beforePrice = product.getBeforePrice(priceType);
        this.priceAdditionalInfo = product.getPriceAdditionalInfo(priceType);
        this.lastUpdateAt = product.getLastUpdateAt(priceType);
    }

}
