package com.podo.helloprice.api.domain.product.dto;

import com.podo.helloprice.api.domain.productsale.ProductSale;
import com.podo.helloprice.core.enums.SaleType;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProductSimpleDto {

    private Long id;
    private List<ProductSaleSimpleDto> productSales;

    public ProductSimpleDto(Long id, List<ProductSale> productSales) {
        this.id = id;
        this.productSales = productSales.stream()
                .map(ProductSaleSimpleDto::new)
                .collect(Collectors.toList());
    }

    @Getter
    private static class ProductSaleSimpleDto {

        private Long id;
        private SaleType saleType;

        private ProductSaleSimpleDto(ProductSale productSale) {
            this.id = productSale.getId();
            this.saleType = productSale.getSaleType();
        }
    }
}
