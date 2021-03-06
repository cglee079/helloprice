package com.podo.helloprice.crawl.worker.value;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.core.enums.ProductSaleStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class CrawledProduct {

    public static final CrawledProduct FAIL = new CrawledProduct();

    private String productName;
    private String productCode;
    private String url;
    private String description;
    private String imageUrl;
    private ProductSaleStatus saleStatus;
    private LocalDateTime crawledAt;
    private Map<SaleType, CrawledProductPrice> saleTypeToPrice;

    @Builder
    public CrawledProduct(String productCode, String url, String description,
                          String productName, String imageUrl, ProductSaleStatus saleStatus,
                          Map<SaleType, CrawledProductPrice> saleTypeToPrice,
                          LocalDateTime crawledAt) {
        this.productCode = productCode;
        this.url = url;
        this.description = description;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.saleStatus = saleStatus;
        this.saleTypeToPrice = saleTypeToPrice;
        this.crawledAt = crawledAt;
    }

    public CrawledProductPrice getProductPriceByType(SaleType saleType) {
        return this.saleTypeToPrice.get(saleType);
    }

}

