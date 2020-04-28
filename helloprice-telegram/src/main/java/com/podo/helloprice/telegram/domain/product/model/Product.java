package com.podo.helloprice.telegram.domain.product.model;

import com.podo.helloprice.core.enums.PriceType;
import com.podo.helloprice.core.enums.ProductAliveStatus;
import com.podo.helloprice.core.enums.ProductSaleStatus;
import com.podo.helloprice.crawl.worker.value.CrawledProduct;
import com.podo.helloprice.crawl.worker.value.CrawledProductPrice;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.podo.helloprice.core.enums.PriceType.*;
import static com.podo.helloprice.core.enums.PriceType.CARD;

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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @MapKey(name = "priceType")
    private Map<PriceType, ProductPrice> priceTypeToPrice = new HashMap<>();

    @OneToMany(mappedBy = "product")
    private List<UserProductNotify> userProductNotifies = new ArrayList<>();

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
                this.updateAllProductPrices(0, lastCrawledAt);
                this.aliveStatus = ProductAliveStatus.PAUSE;
                break;
            case EMPTY_AMOUNT:
                this.updateAllProductPrices(0, lastCrawledAt);
                break;
            case SALE:
                updatePrices(crawledProduct, crawledAt);
        }
    }

    private void updatePrices(CrawledProduct crawledProduct, LocalDateTime crawledAt) {
        updatePrice(crawledProduct, NORMAL, crawledAt);
        updatePrice(crawledProduct, CASH, crawledAt);
        updatePrice(crawledProduct, CARD, crawledAt);
    }

    private boolean updatePrice(CrawledProduct crawledProduct, PriceType priceType, LocalDateTime crawledAt) {
        final CrawledProductPrice price = crawledProduct.getProductPriceByType(priceType);

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

    private void updateAllProductPrices(Integer price, LocalDateTime updateAt) {
        for (ProductPrice productPrice : priceTypeToPrice.values()) {
            productPrice.update(price, "", updateAt);
        }
    }

    public void addProductPrice(ProductPrice productPrice) {
        this.priceTypeToPrice.put(productPrice.getPriceType(), productPrice);
        productPrice.setProduct(this);
    }

    public void addUserProductNotify(UserProductNotify userProductNotify) {
        this.userProductNotifies.add(userProductNotify);
        this.aliveStatus = ProductAliveStatus.ALIVE;
    }

    public void removeUserProductNotify(UserProductNotify userProductNotify) {
        this.userProductNotifies.remove(userProductNotify);

        if (!hasAnyNotify() && isAlive()) {
            log.debug("APP :: {}({}) 상품은, 어떤 사용자에게도 알림이 없습니다", this.productName, this.productCode);
            log.debug("APP :: {}({}) 상품을, 더 이상 갱신하지 않습니다.(PAUSE)", this.productName, this.productCode);

            this.aliveStatus = ProductAliveStatus.PAUSE;
        }
    }

    private boolean hasAnyNotify() {
        return !this.userProductNotifies.isEmpty();
    }

    private boolean isAlive() {
        return this.aliveStatus.equals(ProductAliveStatus.ALIVE);
    }

    public ProductPrice getPriceByType(PriceType priceType) {
        return this.priceTypeToPrice.get(priceType);
    }


}
