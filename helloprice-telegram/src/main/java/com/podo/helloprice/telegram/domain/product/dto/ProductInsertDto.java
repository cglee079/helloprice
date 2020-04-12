package com.podo.helloprice.telegram.domain.product.dto;

import com.podo.helloprice.code.model.ProductSaleStatus;
import com.podo.helloprice.telegram.domain.product.Product;
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
    private Integer price;
    private ProductSaleStatus saleStatus;

    @Builder
    public ProductInsertDto(String productCode,
                  String url, String productName, String description,
                  String imageUrl, Integer price,
                  ProductSaleStatus saleStatus
    ) {
        this.productCode = productCode;
        this.url = url;
        this.productName = productName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.saleStatus = saleStatus;
    }

    public Product toEntity() {
        return Product.builder()
                .productName(productName)
                .productCode(productCode)
                .url(url)
                .description(description)
                .imageUrl(imageUrl)
                .price(price)
                .saleStatus(saleStatus)
                .lastCrawledAt(LocalDateTime.now())
                .build();
    }

}
