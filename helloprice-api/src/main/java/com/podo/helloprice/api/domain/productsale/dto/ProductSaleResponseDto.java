package com.podo.helloprice.api.domain.productsale.dto;

import com.podo.helloprice.api.domain.productsale.ProductSale;
import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.core.util.MathUtil;
import lombok.Getter;

@Getter
public class ProductSaleResponseDto {

    private Long id;
    private SaleType saleType;
    private Integer price;
    private Integer prevPrice;
    private Double priceChangeRate;
    private String additionalInfo;
    private Boolean notifyOn;

    public ProductSaleResponseDto(ProductSale productSale, boolean notifyOn) {
        this.id = productSale.getId();
        this.saleType = productSale.getSaleType();
        this.price = productSale.getPrice();
        this.prevPrice = productSale.getPrevPrice();
        this.priceChangeRate = MathUtil.divide((price - prevPrice) * 100, prevPrice);
        this.additionalInfo = productSale.getAdditionalInfo();
        this.notifyOn = notifyOn;
    }
}

