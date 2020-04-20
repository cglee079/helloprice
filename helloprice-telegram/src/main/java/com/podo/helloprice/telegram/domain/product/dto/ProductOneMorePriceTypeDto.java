package com.podo.helloprice.telegram.domain.product.dto;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.core.model.ProductAliveStatus;
import com.podo.helloprice.core.model.ProductSaleStatus;
import com.podo.helloprice.telegram.domain.product.model.Product;
import com.podo.helloprice.telegram.domain.product.model.ProductPrice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ProductOneMorePriceTypeDto {

    private Long id;
    private String productName;
    private String productCode;
    private String description;
    private String url;
    private String imageUrl;
    private Map<PriceType, Price> prices = new HashMap<>();
    private ProductAliveStatus aliveStatus;
    private ProductSaleStatus saleStatus;
    private LocalDateTime lastCrawledAt;

    public ProductOneMorePriceTypeDto(Product product){
        this.id = product.getId();
        this.productName = product.getProductName();
        this.productCode = product.getProductCode();
        this.description = product.getDescription();
        this.url = product.getUrl();
        this.imageUrl = product.getImageUrl();
        this.aliveStatus = product.getAliveStatus();
        this.saleStatus = product.getSaleStatus();
        this.lastCrawledAt = product.getLastCrawledAt();

        final Map<PriceType, ProductPrice> productPrices = product.getProductPrices();
        for (PriceType priceType : productPrices.keySet()) {
            this.prices.put(priceType, new  Price(productPrices.get(priceType)));
        }
    }

    @AllArgsConstructor
    @Getter
    public static class Price{
        private PriceType priceType;
        private Integer price;
        private String additionalInfo;

        public Price(ProductPrice productPrice) {
            this.priceType = productPrice.getPriceType();
            this.price = productPrice.getPrice();
            this.additionalInfo = productPrice.getAdditionalInfo();
        }
    }

}
