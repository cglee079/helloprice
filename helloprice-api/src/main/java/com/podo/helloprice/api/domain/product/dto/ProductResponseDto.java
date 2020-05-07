package com.podo.helloprice.api.domain.product.dto;

import com.podo.helloprice.api.domain.product.model.Product;
import com.podo.helloprice.api.domain.productsale.ProductSale;
import com.podo.helloprice.api.domain.productsale.ProductSaleResponseDto;
import com.podo.helloprice.core.enums.ProductSaleStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProductResponseDto {

    private Long id;
    private String productCode;
    private String productName;
    private String url;
    private String imageUrl;
    private String description;
    private ProductSaleStatus saleStatus;
    private LocalDateTime lastConfirmAt;
    private List<ProductSaleResponseDto> productSales;

    public ProductResponseDto(Product product, List<ProductSale> productSales) {
        this.id = product.getId();
        this.productCode = product.getProductCode();
        this.productName = product.getProductName();
        this.url = product.getUrl();
        this.imageUrl = product.getImageUrl();
        this.lastConfirmAt = product.getLastCrawledAt();
        this.saleStatus = product.getSaleStatus();
        this.description = product.getDescription();
        this.productSales = productSales.stream()
                .map(ProductSaleResponseDto::new)
                .collect(Collectors.toList());
    }
}
