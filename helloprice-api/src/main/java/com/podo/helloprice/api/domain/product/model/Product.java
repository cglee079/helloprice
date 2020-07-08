package com.podo.helloprice.api.domain.product.model;

import com.podo.helloprice.core.enums.ProductAliveStatus;
import com.podo.helloprice.core.enums.ProductSaleStatus;
import com.podo.helloprice.crawl.worker.value.CrawledProduct;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productCode;

    private String productName;

    private String description;

    private String url;

    private String imageUrl;

    private LocalDateTime lastCrawledAt;

    private LocalDateTime lastPublishAt;

    private Integer deadCount;

    @Enumerated(EnumType.STRING)
    private ProductAliveStatus aliveStatus;

    @Enumerated(EnumType.STRING)
    private ProductSaleStatus saleStatus;

    @Builder
    public Product(String productCode, String productName,
                   String description, String url, String imageUrl,
                   LocalDateTime lastCrawledAt, LocalDateTime lastPublishAt,
                   Integer deadCount, ProductAliveStatus aliveStatus, ProductSaleStatus saleStatus) {
        this.productCode = productCode;
        this.productName = productName;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
        this.lastCrawledAt = lastCrawledAt;
        this.lastPublishAt = lastPublishAt;
        this.deadCount = deadCount;
        this.aliveStatus = aliveStatus;
        this.saleStatus = saleStatus;
    }

    public void pause() {
        this.aliveStatus = ProductAliveStatus.PAUSE;
    }

    public void updateByCrawledProduct(CrawledProduct crawledProduct) {
        this.deadCount = 0;
        this.productName = crawledProduct.getProductName();
        this.imageUrl = crawledProduct.getImageUrl();
        this.saleStatus = crawledProduct.getSaleStatus();
        this.lastCrawledAt =  crawledProduct.getCrawledAt();

        switch (saleStatus) {
            case UNKNOWN:
            case DISCONTINUE:
            case NOT_SUPPORT:
                this.aliveStatus = ProductAliveStatus.PAUSE;
                break;
            case EMPTY_AMOUNT:
            case SALE:
                this.aliveStatus = ProductAliveStatus.ALIVE;
                break;
            default:
        }
    }
}
