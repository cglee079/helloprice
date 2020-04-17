package com.podo.helloprice.telegram.domain.product.dto;

import com.podo.helloprice.core.model.ProductSaleStatus;
import com.podo.helloprice.telegram.domain.product.model.Product;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class ProductInsertDto {

    private String productName;
    private String productCode;
    private String url;
    private String description;
    private String imageUrl;
    private ProductSaleStatus saleStatus;

    @Builder
    public ProductInsertDto(String productCode,
                  String url, String productName, String description,
                  String imageUrl, ProductSaleStatus saleStatus
    ) {
        this.productCode = productCode;
        this.url = url;
        this.productName = productName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.saleStatus = saleStatus;
    }

    public Product toEntity() {
        return Product.builder()
                .productName(productName)
                .productCode(productCode)
                .url(url)
                .description(description)
                .imageUrl(imageUrl)
                .saleStatus(saleStatus)
                .lastCrawledAt(LocalDateTime.now())
                .build();
    }

}
