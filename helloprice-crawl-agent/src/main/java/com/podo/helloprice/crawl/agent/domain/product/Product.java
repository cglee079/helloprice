package com.podo.helloprice.crawl.agent.domain.product;

import com.podo.helloprice.code.model.ProductAliveStatus;
import com.podo.helloprice.code.model.ProductSaleStatus;
import com.podo.helloprice.code.model.ProductUpdateStatus;
import com.podo.helloprice.crawl.worker.vo.CrawledProduct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
@Entity
public class Product{

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

    @LastModifiedDate
    private LocalDateTime updateAt;

    @LastModifiedBy
    private String updateBy;

    public void updateByCrawledItem(CrawledProduct crawledProduct) {
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
                updateProductAt(lastCrawledAt);
                break;

            case SALE:
            case EMPTY_AMOUNT:
                if (existPrice.equals(crawledPrice)) {
                    updateStatus = ProductUpdateStatus.BE;
                } else {
                    beforePrice = existPrice;
                    updateProductAt(lastCrawledAt);
                }
            default:
                break;
        }
    }

    public void increaseDeadCount(Integer maxDeadCount, LocalDateTime lastCrawledAt) {
        this.deadCount++;

        if(this.deadCount > maxDeadCount){
            log.debug("{}({}) 상품 DEAD_COUNT 초과, DEAD 상태로 변경", this.productName, this.productCode);
            this.beforePrice = this.price;
            this.price = 0;
            this.aliveStatus = ProductAliveStatus.DEAD;
            updateProductAt(lastCrawledAt);
        }
    }

    private void updateProductAt(LocalDateTime lastUpdatedAt) {
        this.updateStatus = ProductUpdateStatus.UPDATED;
        this.lastUpdatedAt = lastUpdatedAt;
    }

}
