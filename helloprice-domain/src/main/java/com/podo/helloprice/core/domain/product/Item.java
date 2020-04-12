package com.podo.helloprice.core.domain.product;

import com.podo.helloprice.code.model.ProductAliveStatus;
import com.podo.helloprice.code.model.ProductSaleStatus;
import com.podo.helloprice.code.model.ProductUpdateStatus;
import com.podo.helloprice.core.domain.BaseEntity;
import com.podo.helloprice.core.domain.useritem.UserProductNotify;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "item")
@Entity
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemCode;

    private String itemName;

    private String itemDesc;

    private String itemUrl;

    private String itemImage;

    private Integer itemPrice;

    private Integer itemBeforePrice;

    private LocalDateTime lastCrawledAt;

    private LocalDateTime lastPublishAt;

    private LocalDateTime lastUpdatedAt;

    private Integer deadCount;

    @Enumerated(EnumType.STRING)
    private ProductAliveStatus productAliveStatus;

    @Enumerated(EnumType.STRING)
    private ProductSaleStatus itemSaleStatus;

    @Enumerated(EnumType.STRING)
    private ProductUpdateStatus itemUpdateStatus;

    @OneToMany(mappedBy = "item")
    private List<UserProductNotify> userProductNotifies = new ArrayList<>();

    @Builder
    public Product(String itemCode,
                String itemName, String itemDesc,
                String itemUrl, String itemImage,
                Integer itemPrice,
                ProductSaleStatus itemSaleStatus,
                LocalDateTime lastCrawledAt) {

        this.itemCode = itemCode;
        this.itemUrl = itemUrl;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemImage = itemImage;
        this.itemPrice = itemPrice;
        this.itemBeforePrice = itemPrice;
        this.lastCrawledAt = lastCrawledAt;
        this.lastUpdatedAt = lastCrawledAt;
        this.lastPublishAt = lastCrawledAt;
        this.itemSaleStatus = itemSaleStatus;
        this.deadCount = 0;
        this.productAliveStatus = ProductAliveStatus.ALIVE;
        this.itemUpdateStatus = ProductUpdateStatus.BE;

    }
    public void updateByCrawledProduct(CrawledProduct crawledProduct) {
        final Integer existPrice = this.itemPrice;
        final Integer crawledPrice = crawledProduct.getProductPrice();

        this.itemPrice = crawledPrice;
        this.deadCount = 0;

        this.itemName = crawledProduct.getProductName();
        this.itemImage = crawledProduct.getProductImage();
        this.lastCrawledAt = crawledProduct.getCrawledAt();
        this.itemSaleStatus = crawledProduct.getProductSaleStatus();

        switch (crawledProduct.getProductSaleStatus()) {
            case UNKNOWN:
            case DISCONTINUE:
            case NOT_SUPPORT:
                this.productAliveStatus = ProductAliveStatus.PAUSE;
                updateProductUpdateInfo(lastCrawledAt);
                break;

            case SALE:
            case EMPTY_AMOUNT:
                if (existPrice.equals(crawledPrice)) {
                    itemUpdateStatus = ProductUpdateStatus.BE;
                } else {
                    itemBeforePrice = existPrice;
                    updateProductUpdateInfo(lastCrawledAt);
                }
            default:
                break;
        }
    }

    public void increaseDeadCount(Integer maxDeadCount, LocalDateTime lastCrawledAt) {
        this.deadCount++;

        if(this.deadCount > maxDeadCount){
            log.info("{}({}) 상품 DEAD_COUNT 초과, DEAD 상태로 변경", this.itemName, this.itemCode);
            this.itemBeforePrice = this.itemPrice;
            this.itemPrice = 0;
            this.productAliveStatus = ProductAliveStatus.DEAD;
            updateProductUpdateInfo(lastCrawledAt);
        }
    }

    private void updateProductUpdateInfo(LocalDateTime lastUpdatedAt) {
        this.itemUpdateStatus = ProductUpdateStatus.UPDATED;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public void addUserProductNotify(UserProductNotify userProductNotify) {
        this.userProductNotifies.add(userProductNotify);
        this.productAliveStatus = ProductAliveStatus.ALIVE;
    }

    public void removeUserProductNotify(UserProductNotify userProductNotify) {
        this.userProductNotifies.remove(userProductNotify);

        if (!hasAnyNotify() && isAlive()) {
            log.info("{}({}) 상품은, 어떤 사용자에게도 알림이 없습니다", this.itemName, this.itemCode);
            log.info("{}({}) 상품을, 더 이상 갱신하지 않습니다.(중지)", this.itemName, this.itemCode);

            this.productAliveStatus = ProductAliveStatus.PAUSE;
        }
    }

    private boolean hasAnyNotify() {
        return !this.userProductNotifies.isEmpty();
    }

    private boolean isAlive() {
        return this.productAliveStatus.equals(ProductAliveStatus.ALIVE);
    }

    public void notified() {
        this.itemUpdateStatus = ProductUpdateStatus.BE;
    }

    public void updateLastPublishAt(LocalDateTime lastPublishAt){
        this.lastPublishAt = lastPublishAt;
    }
}
