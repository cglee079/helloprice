package com.podo.helloprice.crawl.agent.domain.product;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.core.enums.ProductAliveStatus;
import com.podo.helloprice.core.enums.ProductSaleStatus;
import com.podo.helloprice.crawl.agent.domain.productsale.ProductSale;
import com.podo.helloprice.crawl.worker.value.CrawledProduct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@EntityListeners(AuditingEntityListener.class)
@Slf4j
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

    private Integer deadCount;

    @Enumerated(EnumType.STRING)
    private ProductAliveStatus aliveStatus;

    @Enumerated(EnumType.STRING)
    private ProductSaleStatus saleStatus;

    @LastModifiedDate
    private LocalDateTime updateAt;

    @LastModifiedBy
    private String updateBy;

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
                break;
        }
    }

    public boolean increaseDeadCount(Integer maxDeadCount) {
        this.deadCount++;

        if (this.deadCount > maxDeadCount) {
            log.debug("{}({}) 상품 DEAD_COUNT 초과, DEAD 상태로 변경", this.productName, this.productCode);
            this.aliveStatus = ProductAliveStatus.DEAD;
            return true;
        }

        return false;
    }


}
