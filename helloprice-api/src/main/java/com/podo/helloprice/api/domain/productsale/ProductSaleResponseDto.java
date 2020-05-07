package com.podo.helloprice.api.domain.productsale;

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

    public ProductSaleResponseDto(ProductSale productSale) {
        this.id = productSale.getId();
        this.saleType = productSale.getSaleType();
        this.price = productSale.getPrice();
        this.prevPrice = productSale.getPrevPrice();
        this.priceChangeRate = MathUtil.divide(price - prevPrice, price);
        this.additionalInfo = productSale.getAdditionalInfo();
    }
}

