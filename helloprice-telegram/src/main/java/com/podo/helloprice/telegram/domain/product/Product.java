package com.podo.helloprice.telegram.domain.product;

import com.podo.helloprice.code.model.ProductAliveStatus;
import com.podo.helloprice.code.model.ProductSaleStatus;
import com.podo.helloprice.code.model.ProductUpdateStatus;
import com.podo.helloprice.crawl.worker.vo.CrawledProduct;
import com.podo.helloprice.telegram.domain.BaseEntity;
import com.podo.helloprice.telegram.domain.userproduct.UserProductNotify;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.podo.helloprice.code.model.ProductUpdateStatus.BE;

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

    private Integer price;

    private Integer beforePrice;

    private LocalDateTime lastCrawledAt;

    private LocalDateTime lastPublishAt;

    private LocalDateTime lastUpdatedAt;

    private Integer deadCount;

    @Enumerated(EnumType.STRING)
    private ProductAliveStatus aliveStatus;

    @Enumerated(EnumType.STRING)
    private ProductSaleStatus saleStatus;

    @Enumerated(EnumType.STRING)
    private ProductUpdateStatus updateStatus;

    @OneToMany(mappedBy = "product")
    private List<UserProductNotify> userProductNotifies = new ArrayList<>();

    @Builder
    public Product(String productCode,
                   String productName, String description,
                   String url, String imageUrl,
                   Integer price,
                   ProductSaleStatus saleStatus,
                   LocalDateTime lastCrawledAt) {

        this.productCode = productCode;
        this.url = url;
        this.productName = productName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.beforePrice = price;
        this.lastCrawledAt = lastCrawledAt;
        this.lastUpdatedAt = lastCrawledAt;
        this.lastPublishAt = lastCrawledAt;
        this.saleStatus = saleStatus;
        this.deadCount = 0;
        this.aliveStatus = ProductAliveStatus.ALIVE;
        this.updateStatus = BE;
    }

    public void updateByCrawledProduct(CrawledProduct crawledProduct) {
        final Integer existPrice = this.price;
        final Integer crawledPrice = crawledProduct.getPrice();

        this.price = crawledPrice;
        this.deadCount = 0;

        this.productName = crawledProduct.getProductName();
        this.imageUrl = crawledProduct.getImageUrl();
        this.lastCrawledAt = crawledProduct.getCrawledAt();
        this.saleStatus = crawledProduct.getSaleStatus();

        switch (crawledProduct.getSaleStatus()) {
            case UNKNOWN:
            case DISCONTINUE:
            case NOT_SUPPORT:
                this.aliveStatus = ProductAliveStatus.PAUSE;
                updateProductUpdateInfo(lastCrawledAt);
                break;

            case SALE:
            case EMPTY_AMOUNT:
                if (existPrice.equals(crawledPrice)) {
                    updateStatus = BE;
                } else {
                    beforePrice = existPrice;
                    updateProductUpdateInfo(lastCrawledAt);
                }
            default:
                break;
        }
    }

    private void updateProductUpdateInfo(LocalDateTime lastUpdatedAt) {
        this.updateStatus = ProductUpdateStatus.UPDATED;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public void addUserProductNotify(UserProductNotify userProductNotify) {
        this.userProductNotifies.add(userProductNotify);
        this.aliveStatus = ProductAliveStatus.ALIVE;
    }

    public void removeUserProductNotify(UserProductNotify userProductNotify) {
        this.userProductNotifies.remove(userProductNotify);

        if (!hasAnyNotify() && isAlive()) {
            log.debug("APP :: {}({}) 상품은, 어떤 사용자에게도 알림이 없습니다", this.productName, this.productCode);
            log.debug("APP :: {}({}) 상품을, 더 이상 갱신하지 않습니다.(중지)", this.productName, this.productCode);

            this.aliveStatus = ProductAliveStatus.PAUSE;
        }
    }

    private boolean hasAnyNotify() {
        return !this.userProductNotifies.isEmpty();
    }

    private boolean isAlive() {
        return this.aliveStatus.equals(ProductAliveStatus.ALIVE);
    }

    public void notified() {
        this.updateStatus = BE;
    }
}
