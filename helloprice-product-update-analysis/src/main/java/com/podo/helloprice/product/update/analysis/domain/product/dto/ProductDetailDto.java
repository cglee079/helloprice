package com.podo.helloprice.product.update.analysis.domain.product.dto;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.core.enums.ProductAliveStatus;
import com.podo.helloprice.core.enums.ProductSaleStatus;
import com.podo.helloprice.product.update.analysis.domain.product.model.Product;
import com.podo.helloprice.product.update.analysis.domain.productsale.ProductSale;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

//가격정보를 포함하고 있음.
@Getter
public class ProductDetailDto {

    private Long id;
    private String productName;
    private String productCode;
    private String description;
    private SaleType saleType;
    private String url;
    private String imageUrl;
    private Integer price;
    private Integer prevPrice;
    private String priceAdditionalInfo;
    private ProductAliveStatus aliveStatus;
    private ProductSaleStatus saleStatus;
    private LocalDateTime lastUpdateAt;
    private LocalDateTime lastCrawledAt;

    @Builder
    public ProductDetailDto(Product product, SaleType saleType) {
        this.id = product.getId();
        this.aliveStatus = product.getAliveStatus();
        this.productCode = product.getProductCode();
        this.productName = product.getProductName();
        this.url = product.getUrl();
        this.description = product.getDescription();
        this.imageUrl = product.getImageUrl();
        this.saleStatus = product.getSaleStatus();
        this.lastCrawledAt = product.getLastCrawledAt();
        this.saleType = saleType;

        final ProductSale priceByType = product.getProductPriceByType(saleType);
        this.price = priceByType.getPrice();
        this.prevPrice = priceByType.getPrevPrice();
        this.priceAdditionalInfo = priceByType.getAdditionalInfo();
        this.lastUpdateAt = priceByType.getLastUpdateAt();
    }

}
