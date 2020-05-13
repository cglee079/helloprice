package com.podo.helloprice.api.domain.productsale.dto;

import com.podo.helloprice.api.domain.product.model.Product;
import com.podo.helloprice.api.domain.productsale.ProductSale;
import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.core.util.MathUtil;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductTopDecreaseResponseDto {

    private Long id;
    private Long productId;
    private String productName;
    private String productCode;
    private String url;
    private String imageUrl;
    private SaleType saleType;
    private Integer price;
    private Integer prevPrice;
    private Double priceChangeRate;
    private Boolean notifyOn;
    private LocalDateTime lastUpdateAt;

    public ProductTopDecreaseResponseDto(ProductSale productSale, boolean notifyOn) {
        final Product product = productSale.getProduct();
        final Integer price = productSale.getPrice();
        final Integer prevPrice = productSale.getPrevPrice();

        this.id = productSale.getId();
        this.productId = product.getId();
        this.productCode = product.getProductCode();
        this.productName = product.getProductName();
        this.url = product.getUrl();
        this.imageUrl = product.getImageUrl();
        this.lastUpdateAt = product.getLastCrawledAt();
        this.saleType = productSale.getSaleType();
        this.price = price;
        this.prevPrice = prevPrice;
        this.notifyOn = notifyOn;
        this.priceChangeRate = MathUtil.divide((price - prevPrice) * 100, prevPrice);
    }
}
