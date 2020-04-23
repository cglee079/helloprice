package com.podo.helloprice.crawl.agent.domain.product;

import com.podo.helloprice.core.enums.PriceType;
import com.podo.helloprice.core.enums.ProductAliveStatus;
import com.podo.helloprice.core.enums.ProductSaleStatus;
import com.podo.helloprice.core.enums.ProductUpdateStatus;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.podo.helloprice.core.enums.PriceType.*;
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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @MapKey(name = "priceType")
    private Map<PriceType, ProductPrice> priceTypeToPrice = new HashMap<>();

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

    public List<ProductUpdateStatus> updateByCrawledProduct(CrawledProduct crawledProduct) {
        final LocalDateTime crawledAt = crawledProduct.getCrawledAt();

        this.deadCount = 0;
        this.productName = crawledProduct.getProductName();
        this.imageUrl = crawledProduct.getImageUrl();
        this.saleStatus = crawledProduct.getSaleStatus();
        this.lastCrawledAt = crawledAt;

        switch (saleStatus) {
            case UNKNOWN:
                this.updateAllProductPrices(0, "", lastCrawledAt);
                this.aliveStatus = ProductAliveStatus.PAUSE;
                return singletonList(ProductUpdateStatus.UPDATE_UNKNOWN);
            case DISCONTINUE:
                this.updateAllProductPrices(0, "", lastCrawledAt);
                this.aliveStatus = ProductAliveStatus.PAUSE;
                return singletonList(ProductUpdateStatus.UPDATE_DISCONTINUE);
            case NOT_SUPPORT:
                this.updateAllProductPrices(0, "", lastCrawledAt);
                this.aliveStatus = ProductAliveStatus.PAUSE;
                return singletonList(ProductUpdateStatus.UPDATE_NOT_SUPPORT);
            case EMPTY_AMOUNT:
                this.updateAllProductPrices(0, "", lastCrawledAt);
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

        if (updatePrice(crawledProduct, NORMAL, crawledAt)) {
            productUpdateStatuses.add(ProductUpdateStatus.UPDATE_SALE_NORMAL_PRICE);
        }

        if (updatePrice(crawledProduct, CASH, crawledAt)) {
            productUpdateStatuses.add(ProductUpdateStatus.UPDATE_SALE_CASH_PRICE);
        }

        if (updatePrice(crawledProduct, CARD, crawledAt)) {
            productUpdateStatuses.add(ProductUpdateStatus.UPDATE_SALE_CARD_PRICE);
        }

        return productUpdateStatuses;
    }

    private boolean updatePrice(CrawledProduct crawledProduct, PriceType priceType, LocalDateTime crawledAt) {
        final CrawledProduct.CrawledProductPrice price = crawledProduct.getProductPriceByType(priceType);

        //기존에 있는 경우, 업데이트
        if(priceTypeToPrice.containsKey(priceType) && price != null) {
            return priceTypeToPrice.get(priceType)
                    .update(price.getPrice(), price.getAdditionalInfo(), crawledAt);
        }

        //기존에 없는 경우, 새로 등록
        if(!priceTypeToPrice.containsKey(priceType) && price != null){
            final ProductPrice productPrice = ProductPrice.create(priceType, price.getPrice(), price.getAdditionalInfo(), crawledAt);
            priceTypeToPrice.put(priceType, productPrice);
            productPrice.setProduct(this);
            return true;
        }

        return false;
    }

    public boolean increaseDeadCount(Integer maxDeadCount, LocalDateTime updateAt) {
        this.deadCount++;

        if (this.deadCount > maxDeadCount) {
            log.debug("{}({}) 상품 DEAD_COUNT 초과, DEAD 상태로 변경", this.productName, this.productCode);
            this.updateAllProductPrices(0, "", updateAt);
            this.aliveStatus = ProductAliveStatus.DEAD;
            return true;
        }

        return false;
    }

    private void updateAllProductPrices(Integer price, String additionalInfo, LocalDateTime updateAt) {
        for (ProductPrice productPrice : priceTypeToPrice.values()) {
            productPrice.update(price, additionalInfo, updateAt);
        }
    }

}
