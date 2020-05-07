package com.podo.helloprice.telegram.domain.productsale.dto;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.telegram.domain.product.dto.ProductDto;
import com.podo.helloprice.telegram.domain.product.model.Product;
import com.podo.helloprice.telegram.domain.productsale.ProductSale;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductSaleDto {

    private Long id;
    private ProductDto product;
    private SaleType saleType;
    private Integer price;
    private Integer prevPrice;
    private String additionalInfo;
    private LocalDateTime lastUpdateAt;

    public ProductSaleDto(ProductSale productSale){
        this.id = productSale.getId();
        this.product = new ProductDto(productSale.getProduct());
        this.saleType = productSale.getSaleType();
        this.price = productSale.getPrice();
        this.prevPrice = productSale.getPrevPrice();
        this.additionalInfo = productSale.getAdditionalInfo();
        this.lastUpdateAt =productSale.getLastUpdateAt();
    }
}
