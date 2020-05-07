package com.podo.helloprice.telegram.domain.product.model;

import com.podo.helloprice.core.enums.ProductAliveStatus;
import com.podo.helloprice.core.enums.ProductSaleStatus;
import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.crawl.worker.value.CrawledProduct;
import com.podo.helloprice.crawl.worker.value.CrawledProductPrice;
import com.podo.helloprice.telegram.domain.BaseEntity;
import com.podo.helloprice.telegram.domain.productsale.ProductSale;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.podo.helloprice.core.enums.SaleType.*;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
@Entity
public class Product extends BaseEntity {

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

    @OneToMany(mappedBy = "product")
    @MapKey(name = "saleType")
    private Map<SaleType, ProductSale> saleTypeToPrice = new HashMap<>();

    @Builder(builderMethodName = "newProduct")
    public Product(String productCode,
                   String productName, String description,
                   String url, String imageUrl,
                   ProductSaleStatus saleStatus,
                   LocalDateTime lastCrawledAt) {

        this.productCode = productCode;
        this.url = url;
        this.productName = productName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.lastCrawledAt = lastCrawledAt;
        this.lastPublishAt = lastCrawledAt;
        this.saleStatus = saleStatus;
        this.aliveStatus = ProductAliveStatus.ALIVE;
        this.deadCount = 0;
    }

    public void updateByCrawledProduct(CrawledProduct crawledProduct) {
        final LocalDateTime crawledAt = crawledProduct.getCrawledAt();

        this.deadCount = 0;
        this.productName = crawledProduct.getProductName();
        this.imageUrl = crawledProduct.getImageUrl();
        this.saleStatus = crawledProduct.getSaleStatus();
        this.lastCrawledAt = crawledAt;

        switch (saleStatus) {
            case UNKNOWN:
            case DISCONTINUE:
            case NOT_SUPPORT:
                this.aliveStatus = ProductAliveStatus.PAUSE;
                break;
            case EMPTY_AMOUNT:
            case SALE:
            default:
                break;
        }
    }

    public void putProductSale(SaleType saleType, ProductSale productSale) {
        this.saleTypeToPrice.put(saleType, productSale);
        productSale.setProduct(this);
    }

    public void revive() {
        this.aliveStatus = ProductAliveStatus.ALIVE;
    }

    public void checkSaleNotify() {
        boolean hasAnyNotify = false;

        for (SaleType saleType : saleTypeToPrice.keySet()) {
            if (saleTypeToPrice.get(saleType).hasAnyNotify()) {
                hasAnyNotify = true;
                break;
            }
        }

        if (!hasAnyNotify) {
            this.aliveStatus = ProductAliveStatus.PAUSE;
        }
    }
}
