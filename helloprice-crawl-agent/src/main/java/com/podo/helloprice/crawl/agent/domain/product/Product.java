package com.podo.helloprice.crawl.agent.domain.product;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.core.model.ProductAliveStatus;
import com.podo.helloprice.core.model.ProductSaleStatus;
import com.podo.helloprice.core.model.ProductUpdateStatus;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.podo.helloprice.core.model.PriceType.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

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

    @OneToMany(mappedBy = "product")
    @MapKey(name = "priceType")
    private Map<PriceType, ProductPrice> productPrices;

    private LocalDateTime lastCrawledAt;

    private LocalDateTime lastPublishAt;

    private Integer deadCount;

    @Enumerated(EnumType.STRING)
    private ProductAliveStatus aliveStatus;

    @Enumerated(EnumType.STRING)
    private ProductSaleStatus saleStatus;

    @LastModifiedDate
    private LocalDateTime updateAt;

    @LastModifiedBy
    private String updateBy;

    public List<ProductUpdateStatus> updateByCrawledProduct(CrawledProduct crawledProduct) {
        final LocalDateTime crawledAt = crawledProduct.getCrawledAt();

        this.deadCount = 0;
        this.productName = crawledProduct.getProductName();
        this.imageUrl = crawledProduct.getImageUrl();
        this.saleStatus = crawledProduct.getSaleStatus();
        this.lastCrawledAt = crawledAt;

        switch (saleStatus) {
            case UNKNOWN:
                this.updateAllProductPrices(0, lastCrawledAt);
                this.aliveStatus = ProductAliveStatus.PAUSE;
                return singletonList(ProductUpdateStatus.UPDATE_UNKNOWN);
            case DISCONTINUE:
                this.updateAllProductPrices(0, lastCrawledAt);
                this.aliveStatus = ProductAliveStatus.PAUSE;
                return singletonList(ProductUpdateStatus.UPDATE_DISCONTINUE);
            case NOT_SUPPORT:
                this.updateAllProductPrices(0, lastCrawledAt);
                this.aliveStatus = ProductAliveStatus.PAUSE;
                return singletonList(ProductUpdateStatus.UPDATE_NOT_SUPPORT);
            case EMPTY_AMOUNT:
                this.updateAllProductPrices(0, lastCrawledAt);
                this.aliveStatus = ProductAliveStatus.ALIVE;
                return singletonList(ProductUpdateStatus.UPDATE_EMPTY_AMOUNT);
            case SALE:
                this.aliveStatus = ProductAliveStatus.ALIVE;
                return updatePrices(crawledProduct, crawledAt);
            default:
                return emptyList();
        }
    }

    private List<ProductUpdateStatus> updatePrices(CrawledProduct crawledProduct, LocalDateTime crawledAt) {
        final List<ProductUpdateStatus> productUpdateStatuses = new ArrayList<>();

        if(productPrices.containsKey(NORMAL)){
            if (productPrices.get(NORMAL).update(crawledProduct.getPrice(), null, crawledAt)) {
                productUpdateStatuses.add(ProductUpdateStatus.UPDATE_SALE_NORMAL_PRICE);
            }
        }

        if(productPrices.containsKey(CASH)){
            if (productPrices.get(CASH).update(crawledProduct.getCashPrice(), null, crawledAt)) {
                productUpdateStatuses.add(ProductUpdateStatus.UPDATE_SALE_CASH_PRICE);
            }
        }

        if(productPrices.containsKey(CARD)){
            if (productPrices.get(CARD).update(crawledProduct.getCardPrice(), crawledProduct.getCardType(), crawledAt)) {
                productUpdateStatuses.add(ProductUpdateStatus.UPDATE_SALE_CARD_PRICE);
            }
        }

        return productUpdateStatuses;
    }

    public boolean increaseDeadCount(Integer maxDeadCount, LocalDateTime updateAt) {
        this.deadCount++;

        if (this.deadCount > maxDeadCount) {
            log.debug("{}({}) 상품 DEAD_COUNT 초과, DEAD 상태로 변경", this.productName, this.productCode);
            this.updateAllProductPrices(0, updateAt);
            this.aliveStatus = ProductAliveStatus.DEAD;
            return true;
        }

        return false;
    }

    private void updateAllProductPrices(Integer price, LocalDateTime updateAt) {
        for (ProductPrice productPrice : productPrices.values()) {
            productPrice.update(price, null, updateAt);
        }
    }

}
