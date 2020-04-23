package com.podo.helloprice.telegram.domain.product.dto;

import com.podo.helloprice.core.enums.ProductAliveStatus;
import com.podo.helloprice.core.enums.ProductSaleStatus;
import com.podo.helloprice.core.enums.PriceType;
import com.podo.helloprice.telegram.domain.product.model.Product;
import com.podo.helloprice.telegram.domain.product.model.ProductPrice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductOnePriceTypeDto {

    private Long id;
    private String productName;
    private String productCode;
    private String description;
    private PriceType priceType;
    private String url;
    private String imageUrl;
    private Integer price;
    private Integer prevPrice;
    private String priceAdditionalInfo;
    private ProductAliveStatus aliveStatus;
    private ProductSaleStatus saleStatus;
    private LocalDateTime lastUpdateAt;
    private LocalDateTime lastCrawledAt;

    @Builder
    public ProductOnePriceTypeDto(Product product, PriceType priceType) {
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

        final ProductPrice priceByType = product.getPriceByType(priceType);

        this.price = priceByType.getPrice();
        this.prevPrice = priceByType.getPrevPrice();
        this.priceAdditionalInfo = priceByType.getAdditionalInfo();
        this.lastUpdateAt = priceByType.getLastUpdateAt();
    }

}
