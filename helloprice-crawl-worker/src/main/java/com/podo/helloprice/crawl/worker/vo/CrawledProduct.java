package com.podo.helloprice.crawl.worker.vo;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.core.model.ProductSaleStatus;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;

@ToString
@EqualsAndHashCode
@Getter
public class CrawledProduct {
    private String productName;
    private String productCode;
    private String url;
    private String description;
    private String imageUrl;
    private ProductSaleStatus saleStatus;
    private LocalDateTime crawledAt;
    private Map<PriceType, CrawledProductPrice> priceTypeToPrice;

    @Builder
    public CrawledProduct(String productCode, String url, String description,
                          String productName, String imageUrl, ProductSaleStatus saleStatus,
                          Map<PriceType, CrawledProductPrice> priceTypeToPrice,
                          LocalDateTime crawledAt) {
        this.productCode = productCode;
        this.url = url;
        this.description = description;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.saleStatus = saleStatus;
        this.priceTypeToPrice = priceTypeToPrice;
        this.crawledAt = crawledAt;
    }

    public CrawledProductPrice getPriceByType(PriceType priceType) {
        return this.priceTypeToPrice.get(priceType);
    }

    @Getter
    public static class CrawledProductPrice{
        private Integer price;
        private String additionalInfo;

        public CrawledProductPrice(Integer price) {
            this(price, "");
        }

        public CrawledProductPrice(Integer price, String additionalInfo) {
          this.price = price;
          this.additionalInfo = additionalInfo;
        }
    }
}

