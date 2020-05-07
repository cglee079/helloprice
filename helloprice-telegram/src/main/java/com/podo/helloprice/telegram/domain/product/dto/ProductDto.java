package com.podo.helloprice.telegram.domain.product.dto;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.core.enums.ProductAliveStatus;
import com.podo.helloprice.core.enums.ProductSaleStatus;
import com.podo.helloprice.telegram.domain.product.model.Product;
import com.podo.helloprice.telegram.domain.productsale.ProductSale;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ProductDto {

    private Long id;
    private String productName;
    private String productCode;
    private String description;
    private String url;
    private String imageUrl;
    private ProductAliveStatus aliveStatus;
    private ProductSaleStatus saleStatus;
    private LocalDateTime lastCrawledAt;

    public ProductDto(Product product){
        this.id = product.getId();
        this.productName = product.getProductName();
        this.productCode = product.getProductCode();
        this.description = product.getDescription();
        this.url = product.getUrl();
        this.imageUrl = product.getImageUrl();
        this.aliveStatus = product.getAliveStatus();
        this.saleStatus = product.getSaleStatus();
        this.lastCrawledAt = product.getLastCrawledAt();
    }
}
