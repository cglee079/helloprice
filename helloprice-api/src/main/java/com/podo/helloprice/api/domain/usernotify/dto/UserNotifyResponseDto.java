package com.podo.helloprice.api.domain.usernotify.dto;

import com.podo.helloprice.api.domain.product.model.Product;
import com.podo.helloprice.api.domain.productsale.ProductSale;
import com.podo.helloprice.core.enums.ProductSaleStatus;
import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.core.util.MathUtil;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserNotifyResponseDto {
    private Long id;
    private ProductResponse product;

    public UserNotifyResponseDto(Long id, ProductSale productSale) {
        this.id = id;
        this.product = new ProductResponse(productSale);
    }

    @Getter
    public static class ProductResponse{
        private Long id;
        private String productCode;
        private String productName;
        private String url;
        private String imageUrl;
        private String description;
        private ProductSaleStatus saleStatus;
        private LocalDateTime lastConfirmAt;
        private ProductSaleResponse productSale;

        public ProductResponse(ProductSale productSale) {
            final Product product = productSale.getProduct();

            this.id = product.getId();
            this.productCode = product.getProductCode();
            this.productName = product.getProductName();
            this.url = product.getUrl();
            this.imageUrl = product.getImageUrl();
            this.lastConfirmAt = product.getLastCrawledAt();
            this.saleStatus = product.getSaleStatus();
            this.description = product.getDescription();
            this.lastConfirmAt =product.getLastCrawledAt();

            this.productSale = new ProductSaleResponse(productSale);
        }


        @Getter
        public static class ProductSaleResponse{
            private Long id;
            private SaleType saleType;
            private Integer price;
            private Integer prevPrice;
            private Double priceChangeRate;
            private String additionalInfo;

            public ProductSaleResponse(ProductSale productSale) {

                this.id = productSale.getId();
                this.saleType = productSale.getSaleType();
                this.price = productSale.getPrice();
                this.prevPrice = productSale.getPrevPrice();
                this.priceChangeRate = MathUtil.divide((price - prevPrice) * 100, prevPrice);
                this.additionalInfo = productSale.getAdditionalInfo();
            }
        }
    }

}
